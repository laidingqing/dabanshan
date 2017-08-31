package com.dabanshan.catalog.api

import akka.{Done, NotUsed}
import com.dabanshan.user.api._
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.api.broker.kafka.{KafkaProperties, PartitionKeyStrategy}
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{CircuitBreaker, Service, ServiceCall}
import play.api.libs.json.{Format, Json}

object UserService  {
  val TOPIC_NAME = "users"
}

trait UserService extends Service {

  /**
    * 注册账号
    * @return
    */
  def registration: ServiceCall[CreateUserMessage, User]

  /**
    * 获取用户账号信息
    * @param id
    * @return
    */
  def getUser(id: String): ServiceCall[NotUsed, User]

  /**
    * 更新用户信息
    * @return
    */
  def updateProfile: ServiceCall[CreateProfileMessage, Done]

  /**
    * 获取用户信息
    * @return
    */
  def getProfile: ServiceCall[NotUsed, Profile]

  override final def descriptor = {
    import Service._
    // @formatter:off
    named("users")
      .withCalls(
        restCall(Method.POST, "/api/users/", registration),
        restCall(Method.GET, "/api/users/:id", getUser _),
        restCall(Method.GET, "/api/users/:id/profiles/", getProfile _),
        restCall(Method.PUT, "/api/users/:id/profiles/", updateProfile _)
      )
      .withAutoAcl(true)
      .withCircuitBreaker(CircuitBreaker.PerNode)
    // @formatter:on
  }
}