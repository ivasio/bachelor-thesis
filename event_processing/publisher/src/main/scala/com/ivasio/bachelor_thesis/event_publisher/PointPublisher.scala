package com.ivasio.bachelor_thesis.event_publisher

import java.time.Instant
import java.util.{Properties, UUID}

import com.ivasio.bachelor_thesis.shared.configuration.SourcedPointKafkaProducerConfig
import com.ivasio.bachelor_thesis.shared.models.Junction
import com.ivasio.bachelor_thesis.shared.records.SourcedPoint
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord, RecordMetadata}
import org.apache.kafka.common.serialization.StringSerializer

import scala.collection.immutable.Stream
import scala.math._
import scala.util.Random


object PointPublisher {

  def main(args: Array[String]): Unit = {
    val junction = new Junction(3, "МКАД - Рязанский пр.", 37.83499f, 55.70789f, 1000)
    publishPoints(junction)
  }

  def publishPoints(junction: Junction): Unit = {
    val sourceId = UUID.randomUUID.toString
    val producer = new Producer
    generateCoordinates(junction)
      .map{case (x, y) => new SourcedPoint(sourceId, x, y, Instant.now())}
      .map(producer.send)
      .foreach {point =>
        println(point)
        Thread.sleep(3000)
      }

  }


  def generateCoordinates(junction: Junction): Stream[(Float, Float)] = {
    val radiusCoordinates = DistanceConverter.toCoordinates(junction.getRadius * 2)
    val loops = 3 + Random.nextInt(5)

    def ro(phi: Double): Double = radiusCoordinates * cos(loops * phi)

    Stream
      .iterate(0.0)(_ + 0.1)
      .map(phi => (phi, ro(phi)))
      .map { case (phi, r) => (
        (junction.getLongitude + r * cos(phi)).asInstanceOf[Float],
        (junction.getLatitude + r * sin(phi)).asInstanceOf[Float]
      )}
  }

}


class Producer() {
  val properties: Properties = new SourcedPointKafkaProducerConfig().getProperties
  properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer])
  val producer: KafkaProducer[String, GenericRecord] = new KafkaProducer[String, GenericRecord](properties)

  def send(point: SourcedPoint): RecordMetadata = producer.send(
    new ProducerRecord[String, GenericRecord](
      properties.getProperty("TOPIC_NAME"), point.sourceId, point.toGenericRecord
    )
  ).get()
}


object DistanceConverter {
  val met: Double = 500.0
  val coord: Double = hypot(55.59307 - 55.59037, 37.73252 - 37.72585)

  def toCoordinates(meters: Double): Double = meters / met * coord
  def toMeters(coordinates: Double): Double = coordinates / coord * met
}