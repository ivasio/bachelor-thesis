package com.ivasio.bachelor_thesis.event_aggregator

import com.ivasio.bachelor_thesis.shared.configuration.PostgresHibernateConfig
import com.ivasio.bachelor_thesis.shared.models._
import org.apache.flink.streaming.api.functions.sink.SinkFunction
import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration


class PointsListJDBCSinkFunction extends SinkFunction[(List[Point], Long)] {

  override def invoke(value: (List[Point], Long), context: SinkFunction.Context[_]): Unit = {
    val session = sessionFactory.openSession()
    val tx = session.beginTransaction()

    val junction = session.get(classOf[Junction], value._2)
    val route = new Route()
    route.setJunction(junction)
    session.save(route)

    value._1.foreach(point => {
      point.setRoute(route)
      session.save(point)
    })

    tx.commit()
  }

  lazy val sessionFactory: SessionFactory = {
    val config = new Configuration()
    config.configure()
    config.addProperties(new PostgresHibernateConfig().getProperties)
    config.buildSessionFactory()
  }
}
