package com.dabanshan.catalog.api

import akka.{Done, NotUsed}
import com.dabanshan.commons.response.GeneratedIdDone
import com.dabanshan.user.api._
import com.dabanshan.user.api.model.request.{UserCreation, UserLogin}
import com.dabanshan.user.api.model.response.{CreationUserDone, GetUserDone}
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
  def registration(): ServiceCall[UserCreation, CreationUserDone]

  /**
    * 获取账户信息
    * @param userId
    * @return
    */
  def getUser(userId: String): ServiceCall[NotUsed, GetUserDone]

  override final def descriptor = {
    import Service._
    // @formatter:off
    named("users")
      .withCalls(
        restCall(Method.POST, "/api/users/", registration _),
        restCall(Method.GET, "/api/users/:userId", getUser _)
      )
      .withAutoAcl(true)
      .withCircuitBreaker(CircuitBreaker.PerNode)
    // @formatter:on
  }
}