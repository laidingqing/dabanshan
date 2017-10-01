package com.dabanshan.order.api

import com.dabanshan.order.api.model.request.OrderCreation
import com.dabanshan.order.api.model.response.CreationOrderDone
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

  override final def descriptor = {
    import Service._
    // @formatter:off
    named("orders")
      .withCalls(
        restCall(Method.POST, "/api/orders/", createOrder )
      )
      .withAutoAcl(true)
      .withCircuitBreaker(CircuitBreaker.PerNode)
    // @formatter:on
  }
}