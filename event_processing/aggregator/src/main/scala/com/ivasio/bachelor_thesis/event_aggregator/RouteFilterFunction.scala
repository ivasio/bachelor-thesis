package com.ivasio.bachelor_thesis.event_aggregator


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
