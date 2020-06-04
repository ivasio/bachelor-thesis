package com.ivasio.bachelor_thesis.event_pusher

import com.ivasio.bachelor_thesis.shared.models.Junction

import scala.collection.immutable.Stream
import scala.math._
import scala.util.Random


object  PointPublisher {

  def main(): Unit = {
    val junction = new Junction(1, "name", 34.0f, 45.0f, 1000)
    generatePoints(junction).foreach{ case (x, y) =>
      print(x, y)
      Thread.sleep(2000)
    }
  }


  def generatePoints(junction: Junction) : Stream[(Double, Double)] = {
    val radiusCoordinates = DistanceConverter.toCoordinates(junction.getRadius * 2)
    val loops = 3 + Random.nextInt(5)

    def ro(phi: Double): Double = radiusCoordinates * cos(loops * phi)

    Stream
      .iterate(0.0)(_ + 0.1)
      .map(phi => (phi, ro(phi)))
      .map{ case (phi, r) => (r * cos(phi), r * sin(phi)) }
  }

}


object DistanceConverter {
  val met: Double = 500.0
  val coord: Double = hypot(55.59307 - 55.59037, 37.73252 - 37.72585)

  def toCoordinates(meters: Double): Double = meters / met * coord;
  def toMeters(coordinates: Double): Double = coordinates / coord * met;
}