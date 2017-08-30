package com.dabanshan.catalog.impl

import com.dabanshan.catalog.api.CatalogService
import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server._
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import play.api.libs.ws.ahc.AhcWSComponents
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaComponents
import com.softwaremill.macwire._

class ExamplelagomLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new ExamplelagomApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new ExamplelagomApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[CatalogService])
}

abstract class ExamplelagomApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with CassandraPersistenceComponents
    with LagomKafkaComponents
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer = serverFor[CatalogService](wire[CatalogServiceImpl])

  // Register the JSON serializer registry
  override lazy val jsonSerializerRegistry = ExamplelagomSerializerRegistry

  // Register the example-lagom persistent entity
  persistentEntityRegistry.register(wire[ExamplelagomEntity])
}
