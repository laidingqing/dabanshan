package com.dabanshan.catalog.api

import akka.{Done, NotUsed}
import com.dabanshan.user.api.{LoginUser, ResetPassword, User}
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.api.broker.kafka.{KafkaProperties, PartitionKeyStrategy}
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}
import play.api.libs.json.{Format, Json}

object UserService  {
  val TOPIC_NAME = "users"
}

trait UserService extends Service {

  def registration: ServiceCall[User, Done]
  def getUser(id: String): ServiceCall[NotUsed, User]

  override final def descriptor = {
    import Service._
    // @formatter:off
    named("users")
      .withCalls(
        pathCall("/api/users/", registration),
        pathCall("/api/users/:id", getUser _)
      )
      .withAutoAcl(true)
    // @formatter:on
  }
}