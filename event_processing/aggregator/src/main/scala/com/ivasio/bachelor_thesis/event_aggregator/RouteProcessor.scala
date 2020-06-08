package com.ivasio.bachelor_thesis.event_aggregator

import com.ivasio.bachelor_thesis.shared.configuration._
import com.ivasio.bachelor_thesis.shared.records._
import org.apache.avro.generic.GenericRecord
import org.apache.flink.formats.avro.AvroDeserializationSchema
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.kafka.clients.consumer.ConsumerConfig


object RouteProcessor {
  val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

  def main(args: Array[String]) {
    val junctions = setupJunctionsSourceStream
      .keyBy(_ => 0)

    val points = setupPointsSourceStream
      .assignAscendingTimestamps(_.timestamp.getEpochSecond)
      .keyBy(_.sourceId)  // splitting the stream here
      .keyBy(_ => 0)  // unifying all the keys so that connected streams work out

    points
      .connect(junctions)
      .flatMap(new RouteFilterFunction)
        .keyBy(_._2)
      .window(EventTimeSessionWindows.withGap(Time.minutes(2)))
        .allowedLateness(Time.minutes(3))
        .aggregate(new SquashPointsProcessFunction)
      .addSink(new PointsListJDBCSinkFunction)

    env.execute()
  }

  //noinspection DuplicatedCode
  def setupPointsSourceStream: DataStream[SourcedPoint] = {
    val schemaStore = new SourcedPoint()
    @transient lazy val deserializer = new SourcedPoint()
    val config = new KafkaConsumerConfig().getProperties

    env
      .addSource(
        new FlinkKafkaConsumer[GenericRecord](
          schemaStore.getTopicName,
          AvroDeserializationSchema.forGeneric(schemaStore.getSchema), config
        )
      )
      .map(entry => deserializer.fromGenericRecord(entry))
  }

  //noinspection DuplicatedCode
  def setupJunctionsSourceStream: DataStream[JunctionUpdate] = {
    val schemaStore = new JunctionUpdate()
    @transient lazy val deserializer = new JunctionUpdate()
    val config = new KafkaProducerConfig().getProperties
    config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

    env
      .addSource(
        new FlinkKafkaConsumer[GenericRecord](
          schemaStore.getTopicName,
          AvroDeserializationSchema.forGeneric(schemaStore.getSchema), config
        )
      )
      .map(entry => deserializer.fromGenericRecord(entry))
  }

}
