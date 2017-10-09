package com.dabanshan.order.api

import akka.{Done, NotUsed}
import com.dabanshan.order.api.model.request.{GetOrder, OrderCreation}
import com.dabanshan.order.api.model.response.{CreationOrderDone, GetOrderDone}
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{CircuitBreaker, Service, ServiceCall}

/**
  * Created by skylai on 2017/10/1.
  */
trait OrderService extends Service {

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

  override final def descriptor = {
    import Service._
    // @formatter:off
    named("orders")
      .withCalls(
        restCall(Method.POST, "/api/orders/", createOrder ),
        restCall(Method.POST, "/api/orders/:id", getOrder _),
        restCall(Method.PUT, "/api/orders/:id", updateOrder _)
      )
      .withAutoAcl(true)
      .withCircuitBreaker(CircuitBreaker.PerNode)
    // @formatter:on
  }
}