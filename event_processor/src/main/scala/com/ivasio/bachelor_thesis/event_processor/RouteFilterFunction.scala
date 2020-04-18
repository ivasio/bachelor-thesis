package com.ivasio.bachelor_thesis.event_processor

import com.ivasio.bachelor_thesis.shared.models.Junction
import com.ivasio.bachelor_thesis.shared.serialization.SourcedPoint
import org.apache.flink.api.common.state.{ListState, ListStateDescriptor, MapState, MapStateDescriptor, ValueStateDescriptor}
import org.apache.flink.api.java.tuple.Tuple
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.functions.co.{KeyedCoProcessFunction, RichCoFlatMapFunction}
import org.apache.flink.util.Collector


class RouteFilterFunction extends RichCoFlatMapFunction[SourcedPoint, Junction, (SourcedPoint, Long)] {

  lazy val junctionsStore: MapState[Long, Junction] = getRuntimeContext
    .getMapState(new MapStateDescriptor[Long, Junction]("junctionsStore", classOf[Long], classOf[Junction]))

  override def flatMap1(point: SourcedPoint, out: Collector[(SourcedPoint, Long)]): Unit = {
    junctionsStore.values.forEach(junction => {
      if (junction.containsPoint(point)) out.collect(point, junction.getId)
    })
  }

  override def flatMap2(junction: Junction, out: Collector[(SourcedPoint, Long)]): Unit =
    junctionsStore.put(junction.getId, junction)
}
