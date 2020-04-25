package com.ivasio.bachelor_thesis.event_processor

import com.ivasio.bachelor_thesis.shared.records._
import org.apache.avro.Schema
import org.apache.avro.generic.GenericRecord


trait AvroDeserializable[Record] {
  val schema: Schema
  def fromGenericRecord(genericRecord: GenericRecord): Record
}


object AvroDeserializable {

  implicit val sourcedPoint: AvroDeserializable[SourcedPoint] = new AvroDeserializable[SourcedPoint] {

    override val schema: Schema = new SourcedPoint().getSchema
    override def fromGenericRecord(genericRecord: GenericRecord): SourcedPoint =
      new SourcedPoint().fromGenericRecord(genericRecord)

  }

  implicit val junction: AvroDeserializable[JunctionUpdate] = new AvroDeserializable[JunctionUpdate] {

    override val schema: Schema = new JunctionUpdate().getSchema
    override def fromGenericRecord(genericRecord: GenericRecord): JunctionUpdate =
      new JunctionUpdate().fromGenericRecord(genericRecord)

  }



}