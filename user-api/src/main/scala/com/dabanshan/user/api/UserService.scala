package com.dabanshan.catalog.api

import akka.{Done, NotUsed}
import com.dabanshan.commons.response.GeneratedIdDone
import com.dabanshan.user.api._
import com.dabanshan.user.api.model.request.{TenantCreation, TenantUpdation, UserCreation, UserLogin}
import com.dabanshan.user.api.model.response._
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

  /**
    * 登录
    * @return
    */
  def loginUser(): ServiceCall[UserLogin, LoginUserDone]


  /**
    * 获得用户开通的租户信息
    * @param userId
    * @param tenantId
    * @return
    */
  def getTenant(userId:String, tenantId: String): ServiceCall[NotUsed, GetTenantDone]

  /**
    * 创建用户租户信息
    * @param userId
    * @return
    */
  def createTenant(userId:String): ServiceCall[TenantCreation, CreationTenantDone]

  /**
    * 更新租户信息
    * @param userId
    * @param tenantId
    * @return
    */
  def updateTenant(userId:String, tenantId: String): ServiceCall[TenantUpdation, CreationTenantDone]

  /**
    * 增加租户资质信息
    * @param userId
    * @param tenantId
    * @return
    */
  def addTenantCredentials(userId:String, tenantId: String): ServiceCall[NotUsed, Done]


  override final def descriptor = {
    import Service._
    // @formatter:off
    named("users")
      .withCalls(
        restCall(Method.POST, "/api/users/", registration _),
        restCall(Method.GET, "/api/users/:userId", getUser _),
        restCall(Method.POST, "/api/users/login", loginUser _),

        // with tenants api
        restCall(Method.GET, "/api/users/:userId/tenants/:tenantId", getTenant _ _),
        restCall(Method.POST, "/api/users/:userId/tenants/", createTenant _),
        restCall(Method.PUT, "/api/users/:userId/tenants/:tenantId", updateTenant _ _),
        restCall(Method.POST, "/api/users/:userId/tenants/:tenantId/credentials", addTenantCredentials _ _)
      )
      .withAutoAcl(true)
      .withCircuitBreaker(CircuitBreaker.PerNode)
    // @formatter:on
  }
}