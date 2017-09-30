package com.dabanshan.catalog.api

import akka.{Done, NotUsed}
import com.dabanshan.product.api.model._
import com.dabanshan.product.api.model.request.{CategoryCreation, ProductCreation}
import com.dabanshan.product.api.model.response.{CreationCategoryDone, CreationProductDone, GetAllCategoryDone, GetProductDone}
import com.lightbend.lagom.scaladsl.api.Service._
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.api.broker.kafka.{KafkaProperties, PartitionKeyStrategy}
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{CircuitBreaker, Service, ServiceCall}
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
    * 创建分类
    * @return
    */
  def createCategory: ServiceCall[CategoryCreation, CreationCategoryDone]
  /**
    * 根据编号获取商品信息
    * @return
    */
  def getProduct(): ServiceCall[NotUsed, GetProductDone]

  /**
    * 获取所有分类
    * @param parent 父级编号
    * @return
    */
  def getCategories(parent: String): ServiceCall[NotUsed, GetAllCategoryDone]
  /**
    * 根据分类查询商品
    * @param categoryId
    * @return
    */
  def findProductByCategory(categoryId: String): ServiceCall[NotUsed, GetProductDone]

  override final def descriptor = {
    import Service._
    // @formatter:off
    named("products")
      .withCalls(
        restCall(Method.POST, "/api/products", createProduct),
        restCall(Method.GET, "/api/products/:productId", createProduct),
        restCall(Method.GET, "/api/products", findProductByCategory _),
        restCall(Method.POST, "/api/categories/", createCategory),
        restCall(Method.GET, "/api/categories/", getCategories _)
      )
      .withAutoAcl(true)
      .withCircuitBreaker(CircuitBreaker.PerNode)
    // @formatter:on
  }
}
