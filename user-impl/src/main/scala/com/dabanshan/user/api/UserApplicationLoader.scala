package com.dabanshan.user.api

import com.dabanshan.catalog.api.UserService
import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server._
import play.api.libs.ws.ahc.AhcWSComponents
import play.api.Environment
import com.softwaremill.macwire._
import play.api.mvc.EssentialFilter
import play.filters.cors.CORSComponents

import scala.concurrent.ExecutionContext
/**
  * Created by skylai on 2017/8/30.
  */
class UserApplicationLoader extends LagomApplicationLoader {
  override def load(context: LagomApplicationContext): LagomApplication =
    new UserApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new UserApplication(context) with LagomDevModeComponents

  override def describeServices = List(
    readDescriptor[UserService]
  )
}

trait UserComponents
  extends LagomServerComponents
    with CassandraPersistenceComponents
{
  implicit def executionContext: ExecutionContext
  def environment: Environment

  override lazy val lagomServer = serverFor[UserService](wire[UserServiceImpl])

  override lazy val jsonSerializerRegistry = UserSerializerRegistry

  lazy val userRepository = wire[UserRepository]

  persistentEntityRegistry.register(wire[UserEntity])

  readSide.register(wire[UserEventProcessor])
}

abstract class UserApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with UserComponents
    with AhcWSComponents
    with CORSComponents
{
  override lazy val httpFilters: Seq[EssentialFilter] = Seq(corsFilter)
}