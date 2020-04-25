package com.ivasio.bachelor_thesis.event_processor

import com.ivasio.bachelor_thesis.shared.records._
import org.apache.flink.api.common.state.{MapState, MapStateDescriptor}
import org.apache.flink.streaming.api.functions.co.RichCoFlatMapFunction
import org.apache.flink.util.Collector


class RouteFilterFunction extends RichCoFlatMapFunction[SourcedPoint, JunctionUpdate, (SourcedPoint, Long)] {

  lazy val junctionsStore: MapState[Long, JunctionUpdate] = getRuntimeContext
    .getMapState(new MapStateDescriptor[Long, JunctionUpdate]("junctionsStore", classOf[Long], classOf[JunctionUpdate]))

  override def flatMap1(point: SourcedPoint, out: Collector[(SourcedPoint, Long)]): Unit = {
    junctionsStore.values.forEach(junction => {
      if (junction.containsPoint(point)) out.collect(point, junction.id)
    })
  }

  override def flatMap2(junction: JunctionUpdate, out: Collector[(SourcedPoint, Long)]): Unit =
    junctionsStore.put(junction.id, junction)
}
