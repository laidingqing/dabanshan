package com.dabanshan.balance.api.model

import akka.{Done, NotUsed}
import com.dabanshan.balance.api.model.request.{GetOrder, OrderCreation}
import com.dabanshan.balance.api.model.response.{CreationOrderDone, GetOrderDone}
import com.lightbend.lagom.scaladsl.api.Service._
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{CircuitBreaker, Service, ServiceCall}

/**
  * Created by skylai on 2017/10/1.
  */
trait BalanceService extends Service {

  /**
    * 创建订单
    * @return
    */
  def createOrder(): ServiceCall[OrderCreation, CreationOrderDone]

  /**
    * 获取订单
    * @param id
    * @return
    */
  def getOrder(id: String): ServiceCall[GetOrder, GetOrderDone]

  /**
    * 更新订单(状态)
    * @param id
    * @return
    */
  def updateOrder(id: String): ServiceCall[NotUsed, Done]

  /**
    * 添加至购物车
    * @return
    */
  def addCart():ServiceCall[NotUsed, Done]


  def cartByUser(userId: String):ServiceCall[NotUsed, Done]

  override final def descriptor = {
    import Service._
    // @formatter:off
    named("balances")
      .withCalls(
        // with order
        restCall(Method.POST, "/api/orders/", createOrder ),
        restCall(Method.POST, "/api/orders/:id", getOrder _),
        restCall(Method.PUT, "/api/orders/:id", updateOrder _),
        // with carts
        restCall(Method.POST, "/api/carts/", addCart),
        restCall(Method.GET, "/api/carts/:userId", cartByUser _)

      )
      .withAutoAcl(true)
      .withCircuitBreaker(CircuitBreaker.PerNode)
    // @formatter:on
  }
}