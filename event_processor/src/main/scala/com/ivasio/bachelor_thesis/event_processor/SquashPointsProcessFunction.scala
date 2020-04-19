package com.ivasio.bachelor_thesis.event_processor

import com.ivasio.bachelor_thesis.shared.models.Point
import com.ivasio.bachelor_thesis.shared.serialization.SourcedPoint
import org.apache.flink.api.common.functions.AggregateFunction


class SquashPointsProcessFunction extends AggregateFunction[(SourcedPoint, Long), (List[Point], Long), (List[Point], Long)] {

  override def createAccumulator(): (List[Point], Long) = (List[Point](), 0)

  override def add(in: (SourcedPoint, Long), acc: (List[Point], Long)): (List[Point], Long) =
    (acc._1.appended(in._1), in._2)

  override def merge(acc1: (List[Point], Long), acc2: (List[Point], Long)): (List[Point], Long) =
    (acc1._1.concat(acc2._1), acc1._2)

  override def getResult(acc: (List[Point], Long)): (List[Point], Long) = acc

}