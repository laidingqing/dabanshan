package com.dabanshan.catalog.api

import akka.{Done, NotUsed}
import com.dabanshan.commons.secutiry.CorsFilter
import com.dabanshan.commons.utils.PaginatedSequence
import com.dabanshan.product.api.model.ProductStatus
import com.dabanshan.product.api.model.request.{CategoryCreation, ProductCreation}
import com.dabanshan.product.api.model.response._
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{CircuitBreaker, Service, ServiceCall}

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
  def getProduct(productId: String): ServiceCall[NotUsed, GetProductDone]

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

  /**
    * 添加一张商品缩略图
    * @param productId
    * @return
    */
  def createProductThumbnails(productId: String): ServiceCall[List[String], Done]

  /**
    * 添加商品详细图
    * @param productId
    * @return
    */
  def createProductDetails(productId: String): ServiceCall[NotUsed, Done]

  /**
    * 删除缩略图
    * @param productId
    * @param thumbId
    * @return
    */
  def deleteProductThumbnails(productId: String, thumbId: String ): ServiceCall[NotUsed, Done]

  /**
    * 删除商品详细图
    * @param productId
    * @param detailId
    * @return
    */
  def deleteProductDetails(productId: String, detailId: String ): ServiceCall[NotUsed, Done]

  /**
    * 更新商品信息
    * @param productId
    * @return
    */
  def updateProduct(productId: String): ServiceCall[ProductCreation, CreationProductDone]

  /**
    *
    * @param id
    * @param pageNo
    * @param pageSize
    * @return
    */
  def getProductsForUser(id: String, pageNo: Option[Int], pageSize: Option[Int]): ServiceCall[NotUsed, PaginatedSequence[ProductSummary]]

  override final def descriptor = {
    import Service._
    // @formatter:off
    named("products")
      .withCalls(
        // with products
        restCall(Method.POST, "/api/products/", createProduct),
        pathCall("/api/products/?creatorId&pageNo&pageSize", getProductsForUser _),
        restCall(Method.GET, "/api/products/:productId", getProduct _),
        restCall(Method.PUT, "/api/products/:productId", updateProduct _),
        restCall(Method.POST, "/api/products/:productId/thumbnails", createProductThumbnails _),
        restCall(Method.POST, "/api/products/:productId/thumbnails/:thumbId", deleteProductThumbnails _),
        restCall(Method.POST, "/api/products/:productId/details", createProductDetails _),
        restCall(Method.POST, "/api/products/:productId/details/:detailId", deleteProductDetails _),
        //with categories
        restCall(Method.POST, "/api/categories/", createCategory),
        restCall(Method.GET, "/api/categories/", getCategories _)
      )
      .withAutoAcl(true)
      .withHeaderFilter(new CorsFilter)
      .withCircuitBreaker(CircuitBreaker.PerNode)
    // @formatter:on
  }
}
