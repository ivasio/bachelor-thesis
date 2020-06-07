package com.ivasio.bachelor_thesis.event_aggregator

import com.ivasio.bachelor_thesis.shared.configuration.KafkaProducerConfig
import com.ivasio.bachelor_thesis.shared.records._
import org.apache.avro.generic.GenericRecord
import org.apache.flink.formats.avro.AvroDeserializationSchema
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer


object RouteProcessor {
  val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

  def main(args: Array[String]) {
    val junctions = setupJunctionsSourceStream
    val points = setupPointsSourceStream

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

  def setupPointsSourceStream: DataStream[SourcedPoint] = {
    val schemaStore = new SourcedPoint()
    @transient lazy val deserializer = new SourcedPoint()
    val config = new KafkaProducerConfig()

    env
      .addSource(
        new FlinkKafkaConsumer[GenericRecord](
          schemaStore.getTopicName,
          AvroDeserializationSchema.forGeneric(schemaStore.getSchema), config.getProperties
        )
      )
      .map(entry => deserializer.fromGenericRecord(entry))
  }

  def setupJunctionsSourceStream: DataStream[JunctionUpdate] = {
    val schemaStore = new JunctionUpdate()
    @transient lazy val deserializer = new JunctionUpdate()
    val config = new KafkaProducerConfig()

    env
      .addSource(
        new FlinkKafkaConsumer[GenericRecord](
          schemaStore.getTopicName,
          AvroDeserializationSchema.forGeneric(schemaStore.getSchema), config.getProperties
        )
      )
      .map(entry => deserializer.fromGenericRecord(entry))
  }

}
