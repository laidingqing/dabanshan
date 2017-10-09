package com.dabanshan.tenant.api

import java.util.UUID

import akka.{Done, NotUsed}
import com.dabanshan.tenant.api.model.request.{TenantCreation, TenantUpdation}
import com.dabanshan.tenant.api.model.response.{CreationTenantDone, GetTenantDone}
import com.dabanshan.user.api._
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.api.broker.kafka.{KafkaProperties, PartitionKeyStrategy}
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{CircuitBreaker, Service, ServiceCall}
import play.api.libs.json.{Format, Json}


trait TenantService extends Service {

  def get(tenantId: String): ServiceCall[NotUsed, GetTenantDone]

  def create(): ServiceCall[TenantCreation, CreationTenantDone]

  def update(tenantId: String): ServiceCall[TenantUpdation, CreationTenantDone]

  override final def descriptor = {
    import Service._
    // @formatter:off
    named("tenants")
      .withCalls(
        restCall(Method.GET, "/api/tenants/:id", get _),
        restCall(Method.POST, "/api/tenants/", create),
        restCall(Method.PUT, "/api/tenants/:id", update _)
      )
      .withAutoAcl(true)
      .withCircuitBreaker(CircuitBreaker.PerNode)
    // @formatter:on
  }
}