package com.ivasio.bachelor_thesis.event_processor

import java.util.Properties

import com.ivasio.bachelor_thesis.shared.models.Junction
import com.ivasio.bachelor_thesis.shared.records._
import org.apache.avro.generic.GenericRecord
import org.apache.flink.formats.avro.AvroDeserializationSchema
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer


object RouteProcessor {

  def main(args: Array[String]) {
    val env = setupEnvironments()

    val points = setupKafkaSourceStream[SourcedPoint](env, "source_points")
    val junctions = setupKafkaSourceStream[Junction](env, "source_junctions")

    points
      .connect(junctions)
      .keyBy(_.sourceId, _ => 0)
      .flatMap(new RouteFilterFunction)
      .keyBy(_._2) // todo add event time, lateness
      .window(EventTimeSessionWindows.withGap(Time.minutes(2)))
      .aggregate(new SquashPointsProcessFunction)
      .addSink(new PointsListJDBCSinkFunction)
  }

  def setupEnvironments() : StreamExecutionEnvironment = {
    StreamExecutionEnvironment.getExecutionEnvironment
  }

  def setupKafkaSourceStream[Record](env: StreamExecutionEnvironment, topicName: String)
                                    (implicit avro: AvroDeserializable[Record]) : DataStream[Record] = {
    val kafkaProperties = new Properties()
    kafkaProperties.setProperty("bootstrap.servers", "localhost:9092")
    kafkaProperties.setProperty("group.id", "route_processor")

    env.addSource(
      new FlinkKafkaConsumer[GenericRecord](
        topicName,
        AvroDeserializationSchema.forGeneric(avro.schema), kafkaProperties
      )
    ).map(avro.fromGenericRecord)
  }

}
