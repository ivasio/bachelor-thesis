package com.ivasio.bachelor_thesis.event_processor

import java.util.Properties

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.formats.avro.AvroDeserializationSchema
import com.ivasio.bachelor_thesis.shared.serialization.SourcedPoint
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows
import org.apache.flink.streaming.api.windowing.time.Time


object RouteProcessor {

  val inputTopicName : String = "source_points"

  def main(args: Array[String]) {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val kafkaProperties = new Properties()
    kafkaProperties.setProperty("bootstrap.servers", "localhost:9092")
    kafkaProperties.setProperty("group.id", "test")

    val points = env.addSource(new FlinkKafkaConsumer[SourcedPoint](
      inputTopicName, new AvroDeserializationSchema[SourcedPoint](classOf[SourcedPoint])(), kafkaProperties))
      // todo watermarks

    points
      .keyBy(_.getSourceId)
      .window(EventTimeSessionWindows.withGap(Time.minutes(2)))

  }
}
