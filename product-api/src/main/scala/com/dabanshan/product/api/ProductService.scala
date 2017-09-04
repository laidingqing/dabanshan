package com.dabanshan.catalog.api

import akka.{Done, NotUsed}
import com.dabanshan.product.api.model._
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
  def createProduct: ServiceCall[Product, Done]

  /**
    * 获取所有商品
    * @return
    */
  def getProducts: ServiceCall[NotUsed, Seq[Product]]
  /**
    * 根据编号获取商品信息
    * @return
    */
  def getProduct(): ServiceCall[NotUsed, Product]

  /**
    * 为商品增加分类
    * @param productId
    * @return
    */
  def addCatalog(productId: String): ServiceCall[NewCatalog, Done]

  override final def descriptor = {
    import Service._
    // @formatter:off
    named("products")
      .withCalls(
        restCall(Method.POST, "/api/products", createProduct),
        restCall(Method.GET, "/api/products", getProducts),
        restCall(Method.GET, "/api/products/:productId", createProduct),
        restCall(Method.POST, "/api/products/:productId/catalogs", addCatalog _)
      )
    // @formatter:on
  }
}
