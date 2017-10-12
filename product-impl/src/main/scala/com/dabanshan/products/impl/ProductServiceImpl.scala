package com.dabanshan.products.impl

import akka.{Done, NotUsed}
import com.dabanshan.catalog.api.ProductService
import com.dabanshan.commons.identity.Id
import com.dabanshan.commons.utils.PaginatedSequence
import com.dabanshan.product.api.model.ProductStatus
import com.dabanshan.product.api.model.request.{CategoryCreation, ProductCreation}
import com.dabanshan.product.api.model.response._
import com.dabanshan.products.impl.category.CategoryCommand.{CreateCategory, GetAllCategory}
import com.dabanshan.products.impl.category.CategoryEntity
import com.dabanshan.products.impl.product.ProductCommand.{AddThumbnails, CreateProduct, GetProduct}
import com.dabanshan.products.impl.product.{Product, ProductEntity, ProductRepository}
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.ExecutionContext

/**
  * Created by skylai on 2017/9/30.
  */
class ProductServiceImpl (persistentEntityRegistry: PersistentEntityRegistry,
                          productRepository: ProductRepository)(implicit ec: ExecutionContext) extends ProductService{

  val log: Logger = LoggerFactory.getLogger(getClass)
  private val DefaultPageSize = 10
  /**
    * 创建商品
    *
    * @return
    */
  override def createProduct: ServiceCall[ProductCreation, CreationProductDone] = ServiceCall { request =>
    val productId = Id().randomID.toString
    val ref = productEntityRef(productId)
    val pProduct = Product(productId, request.name, request.price, request.unit, request.category, request.description, ProductStatus.Created, request.thumbnails, request.details, request.creator)
    ref.ask(
      CreateProduct(pProduct)
    )
  }

  /**
    * 根据分类查询商品
    *
    * @param categoryId
    * @return
    */
  override def findProductByCategory(categoryId: String): ServiceCall[NotUsed, GetProductDone] = ???

    /**
    * 获取所有分类
    *
    * @param parent 父级编号
    * @return
    */
  override def getCategories(parent: String): ServiceCall[NotUsed, GetAllCategoryDone]  = ???
  /**
    * 创建分类
    *
    * @return
    */
  override def createCategory(): ServiceCall[CategoryCreation, CreationCategoryDone] = ServiceCall { request =>
    val ref = categoryEntityRef(Id().randomID.toString)
    ref.ask(
      CreateCategory(
        name = request.name
      )
    )
  }

  /**
    * 根据编号获取商品信息
    *
    * @return
    */
  override def getProduct(productId: String): ServiceCall[NotUsed, GetProductDone] = ServiceCall { _ =>
    val ref = productEntityRef(productId)
    ref.ask(GetProduct)
  }


  /**
    *
    * @param id
    * @param pageNo
    * @param pageSize
    * @return
    */
  override def getProductsForUser(id: String, pageNo: Option[Int], pageSize: Option[Int]): ServiceCall[NotUsed, PaginatedSequence[ProductSummary]] = ServiceCall { _ =>
    productRepository.getProductsForUser(id, ProductStatus.Created, pageNo.getOrElse(0), pageSize.getOrElse(DefaultPageSize))
  }

  /**
    * 添加商品缩略图
    *
    * @param productId
    * @return
    */
  override def createProductThumbnails(productId: String): ServiceCall[List[String], Done] = ServiceCall { request =>
    val ref = productEntityRef(productId)
    ref.ask(AddThumbnails(request))
  }

  /**
    * 删除缩略图
    *
    * @param productId
    * @param thumbId
    * @return
    */
override def deleteProductThumbnails(productId: String, thumbId: String): ServiceCall[NotUsed, Done] = ???

  /**
    * 删除商品详细图
    *
    * @param productId
    * @param detailId
    * @return
    */
  override def deleteProductDetails(productId: String, detailId: String): ServiceCall[NotUsed, Done] = ???

  /**
    * 添加商品详细图
    *
    * @param productId
    * @return
    */
  override def createProductDetails(productId: String): ServiceCall[NotUsed, Done] = ???

  /**
    * 更新商品信息
    *
    * @param productId
    * @return
    */
  override def updateProduct(productId: String): ServiceCall[ProductCreation, CreationProductDone] = ???

  private def categoryEntityRef(id: String) = persistentEntityRegistry.refFor[CategoryEntity](id.toString)

  private def productEntityRef(id: String) = persistentEntityRegistry.refFor[ProductEntity](id.toString)

}
