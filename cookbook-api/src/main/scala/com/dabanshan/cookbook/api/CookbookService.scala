package com.dabanshan.cookbook.api

import java.util.UUID

import akka.{Done, NotUsed}
import com.dabanshan.user.api._
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.api.broker.kafka.{KafkaProperties, PartitionKeyStrategy}
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{CircuitBreaker, Service, ServiceCall}
import play.api.libs.json.{Format, Json}


trait CookbookService extends Service {

  /**
    * 获取推荐的菜谱
    * @return
    */
  def getRecommandCookBooks(): ServiceCall[NotUsed, Done]

  /**
    * 分页获取菜谱列表
    * @return
    */
  def getCookBooks(): ServiceCall[NotUsed, Done]

  /**
    * 添加菜谱
    * @return
    */
  def addCookBook(): ServiceCall[NotUsed, Done];

  /**
    * 更新菜谱
    * @param cookbookId
    * @return
    */
  def updateCookBook(cookbookId: UUID): ServiceCall[NotUsed, Done]

  override final def descriptor = {
    import Service._
    // @formatter:off
    named("cookbooks")
      .withCalls(
        restCall(Method.GET, "/api/cookbooks/", getRecommandCookBooks),
        restCall(Method.GET, "/api/cookbooks/all", getCookBooks),
        restCall(Method.POST, "/api/cookbooks/", addCookBook),
        restCall(Method.PUT, "/api/cookbooks/:id", updateCookBook _)
      )
      .withAutoAcl(true)
      .withCircuitBreaker(CircuitBreaker.PerNode)
    // @formatter:on
  }
}