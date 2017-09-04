package com.dabanshan.catalog.api

import akka.{Done, NotUsed}
import com.dabanshan.commons.response.GeneratedIdDone
import com.dabanshan.user.api._
import com.dabanshan.user.api.model.request.{UserCreation, UserLogin}
import com.dabanshan.user.api.model.response.{UserDone, UserLoginDone}
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
  def registration(): ServiceCall[UserCreation, GeneratedIdDone]

  /**
    * 账户登录
    * @return
    */
  def loginUser(): ServiceCall[UserLogin, UserLoginDone]

  /**
    * 获取账户信息
    * @param userId
    * @return
    */
  def getUser(userId: String): ServiceCall[NotUsed, UserDone]

  override final def descriptor = {
    import Service._
    // @formatter:off
    named("users")
      .withCalls(
        restCall(Method.POST, "/api/users/", registration _),
        restCall(Method.GET, "/api/users/:userId", getUser _),
        restCall(Method.POST, "/api/sessions/", loginUser _)
      )
      .withAutoAcl(true)
      .withCircuitBreaker(CircuitBreaker.PerNode)
    // @formatter:on
  }
}