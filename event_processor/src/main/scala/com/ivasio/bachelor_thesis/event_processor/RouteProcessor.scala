package com.ivasio.bachelor_thesis.event_processor

import java.util.Properties

import com.ivasio.bachelor_thesis.shared.records._
import org.apache.avro.generic.GenericRecord
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.formats.avro.AvroDeserializationSchema
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer


object RouteProcessor {
  val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

  def main(args: Array[String]) {
    val points = setupKafkaSourceStream[SourcedPoint]("source_points")
    val junctions = setupKafkaSourceStream[JunctionUpdate]("source_junctions")

    val filteredPoints = points
        .assignAscendingTimestamps(_.timestamp.getEpochSecond)
      .connect(junctions)
      .keyBy(_.sourceId, _ => 0)
      .flatMap(new RouteFilterFunction)

    filteredPoints
      .keyBy(_._2)
      .window(EventTimeSessionWindows.withGap(Time.minutes(2)))
        .allowedLateness(Time.minutes(3))
        .aggregate(new SquashPointsProcessFunction)
      .addSink(new PointsListJDBCSinkFunction)
  }


  def setupKafkaSourceStream[Record : TypeInformation](topicName: String)(implicit avro: AvroDeserializable[Record])
      : DataStream[Record] = {
    val kafkaProperties = new Properties()
    kafkaProperties.setProperty("bootstrap.servers", "localhost:9092")
    kafkaProperties.setProperty("group.id", "route_processor")

    env
      .addSource(
        new FlinkKafkaConsumer[GenericRecord](
          topicName,
          AvroDeserializationSchema.forGeneric(avro.schema), kafkaProperties
        )
      )
      .map(avro.fromGenericRecord _)
  }

}
