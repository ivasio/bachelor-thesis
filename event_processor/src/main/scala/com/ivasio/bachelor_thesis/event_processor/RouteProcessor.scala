package com.ivasio.bachelor_thesis.event_processor

import java.util.Properties

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.formats.avro.AvroDeserializationSchema
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.table.api.EnvironmentSettings
import org.apache.flink.table.api.scala.StreamTableEnvironment
import org.apache.flink.table.descriptors.{Avro, Kafka}

import com.ivasio.bachelor_thesis.shared.models.Junction
import com.ivasio.bachelor_thesis.shared.serialization.{AvroFlinkSerializable, SourcedPoint}


object RouteProcessor {

  def main(args: Array[String]) {
    val (env, tableEnv) = setupEnvironments()

    val points = setupKafkaSourceStream[SourcedPoint](env, "source_points")
    val junctions = setupKafkaSourceStream[Junction](env, "source_points")

    points
      .connect(junctions)
      .keyBy(_.getSourceId, _ => 0)
      .flatMap(new RouteFilterFunction())
      .keyBy(_._2)
      .window(EventTimeSessionWindows.withGap(Time.minutes(2)))
      // WindowedStream -> DataStream doesn't seem to be possible. todo use Table or other workaround
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

  def setupKafkaSourceTable[Record <: AvroFlinkSerializable](tableEnv: StreamTableEnvironment, topicName: String,
                                                             tableName: String, timestampsField: Option[String] = None){
    val tableDescriptor = tableEnv.connect(
      new Kafka()
        .version("universal")
        .topic(topicName)
        .property("bootstrap.servers", "kafka:9092")
        .property("group.id", "route_processor")
        .startFromLatest()
    )
      .withFormat(new Avro().recordClass(Class[Record]))
      .withSchema(Record.getFlinkSchema())
    // .createTemporaryTable(tableName)  // todo add checkpointing
  }

  def setupKafkaSinkTable[Record](tableEnv: StreamTableEnvironment, topicName: String, tableName: String) {
    tableEnv.connect(
      new Kafka()
        .version("universal")
        .topic(topicName)
        .property("bootstrap.servers", "kafka:9092")
        .property("group.id", "route_processor")
        .sinkPartitionerRoundRobin()  // todo improve partitioning
    )
      .withFormat(new Avro().recordClass(Class[Record]))
      .createTemporaryTable(tableName)
  }
}
