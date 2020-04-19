package com.ivasio.bachelor_thesis.event_processor

import java.util.Properties

import com.ivasio.bachelor_thesis.shared.models.Junction
import com.ivasio.bachelor_thesis.shared.serialization.SourcedPoint
import org.apache.flink.api.java.io.jdbc.JDBCOutputFormat
import org.apache.flink.formats.avro.AvroDeserializationSchema
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.table.api.EnvironmentSettings
import org.apache.flink.table.api.scala.StreamTableEnvironment


object RouteProcessor {

  def main(args: Array[String]) {
    val (env, tableEnv) = setupEnvironments()

    val points = setupKafkaSourceStream[SourcedPoint](env, "source_points")
    val junctions = setupKafkaSourceStream[Junction](env, "source_junctions")

    points
      .connect(junctions)
      .keyBy(_.getSourceId, _ => 0)
      .flatMap(new RouteFilterFunction)
      .keyBy(_._2) // todo add event time, lateness
      .window(EventTimeSessionWindows.withGap(Time.minutes(2)))
      .aggregate(new SquashPointsProcessFunction)
      .addSink(new PointsListJDBCSinkFunction)
  }

  def setupEnvironments() : (StreamExecutionEnvironment, StreamTableEnvironment) = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val bsSettings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build()
    (env, StreamTableEnvironment.create(env, bsSettings))
  }

  def setupKafkaSourceStream[Record](env: StreamExecutionEnvironment, topicName: String) : DataStream[Record] = {
    val kafkaProperties = new Properties()
    kafkaProperties.setProperty("bootstrap.servers", "localhost:9092")
    kafkaProperties.setProperty("group.id", "route_processor")

    env.addSource(
      new FlinkKafkaConsumer[Record](
        topicName,
        new AvroDeserializationSchema[Record](classOf[Record])(), kafkaProperties
      )
    ) // todo add event time, watermarks

  }

  def setupJDBCOutputFormat(): JDBCOutputFormat = {
    JDBCOutputFormat.buildJDBCOutputFormat()
      .setDrivername("org.postgresql.Driver")
      .setDBUrl("jdbc:postgresql://host:port/database_name")
      .setUsername("user")
      .setPassword("password")
      .setQuery("INSERT INTO table VALUES (?, ?, ?, ?)")
      .setBatchInterval(100)
      .finish()
  }

}
