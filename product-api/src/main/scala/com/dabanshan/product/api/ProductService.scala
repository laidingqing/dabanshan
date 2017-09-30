package com.dabanshan.catalog.api

import akka.{Done, NotUsed}
import com.dabanshan.product.api.model._
import com.dabanshan.product.api.model.request.ProductCreation
import com.dabanshan.product.api.model.response.{CreationProductDone, GetProductDone}
import com.lightbend.lagom.scaladsl.api.Service._
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.api.broker.kafka.{KafkaProperties, PartitionKeyStrategy}
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}
import play.api.libs.json.{Format, Json}

import scala.collection.immutable

object ProductService  {
  val TOPIC_NAME = "products"
}

trait ProductService extends Service {

  /**
    * 创建商品
    * @return
    */
  def createProduct: ServiceCall[ProductCreation, CreationProductDone]
  /**
    * 根据编号获取商品信息
    * @return
    */
  def getProduct(): ServiceCall[NotUsed, GetProductDone]

  /**
    * 根据分类查询商品
    * @param categoryId
    * @return
    */
  def findProductByCategory(categoryId: String): ServiceCall[NotUsed, Seq[GetProductDone]]

  override final def descriptor = {
    import Service._
    // @formatter:off
    named("products")
      .withCalls(
        restCall(Method.POST, "/api/products", createProduct),
        restCall(Method.GET, "/api/products/:productId", createProduct),
        restCall(Method.GET, "/api/products", findProductByCategory _)
      )
    // @formatter:on
  }
}
