package com.dabanshan.products.api

import com.dabanshan.catalog.api.ProductService
import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomApplicationLoader, LagomServerComponents}
import com.softwaremill.macwire._
import play.api.Environment
import play.api.libs.ws.ahc.AhcWSComponents

import scala.concurrent.ExecutionContext

/**
  * Created by skylai on 2017/9/30.
  */
class ProductApplicationLoader extends LagomApplicationLoader {
  override def load(context: LagomApplicationContext): LagomApplication =
    new ProductApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new ProductApplication(context) with LagomDevModeComponents

  override def describeServices = List(
    readDescriptor[ProductService]
  )
}

trait ProductComponents
  extends LagomServerComponents
    with CassandraPersistenceComponents
{
  implicit def executionContext: ExecutionContext
  def environment: Environment

  override lazy val lagomServer = serverFor[ProductService](wire[ProductServiceImpl])

  override lazy val jsonSerializerRegistry = ProductSerializerRegistry

  lazy val productRepository = wire[ProductRepository]

  persistentEntityRegistry.register(wire[ProductEntity])
  readSide.register(wire[UserEventProcessor])
}

abstract class ProductApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with ProductComponents
    with AhcWSComponents
{
}