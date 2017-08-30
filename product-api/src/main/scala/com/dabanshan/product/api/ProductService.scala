package com.dabanshan.catalog.api

import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.Service._
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.api.broker.kafka.{KafkaProperties, PartitionKeyStrategy}
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}
import play.api.libs.json.{Format, Json}

object ProductService  {
  val TOPIC_NAME = "products"
}

trait ProductService extends Service {

  /**
    * 创建商品
    * @return
    */
  def createProduct: ServiceCall[ProductMessage, Done]

  /**
    * 根据编号获取商品信息
    * @param productId
    * @return
    */
  def getProduct(productId: String): ServiceCall[NotUsed, Product]

  /**
    * 为商品增加分类
    * @param productId
    * @return
    */
  def addCatalog(productId: String): ServiceCall[CatalogId, Done]

  override final def descriptor = {
    import Service._
    // @formatter:off
    named("products")
      .withCalls(
        pathCall("/api/products", createProduct),
        pathCall("/api/products/:productId", createProduct),
        pathCall("/api/products/:productId/catalogs", addCatalog _)
      )
    // @formatter:on
  }
}

case class ProductMessage(message: String)

object ProductMessage {
  implicit val format: Format[ProductMessage] = Json.format[ProductMessage]
}

case class Product(productId: String, name: String)

object Product {
  implicit val format: Format[Product] = Json.format
}

case class CatalogId(id: String)

object CatalogId {
  implicit val format: Format[CatalogId] = Json.format
}