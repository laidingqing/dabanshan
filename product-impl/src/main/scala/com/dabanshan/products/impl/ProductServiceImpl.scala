package com.dabanshan.products.impl

import akka.{Done, NotUsed}
import com.dabanshan.catalog.api.ProductService
import com.dabanshan.commons.identity.Id
import com.dabanshan.product.api.model.request.{CategoryCreation, ProductCreation}
import com.dabanshan.product.api.model.response.{CreationCategoryDone, CreationProductDone, GetAllCategoryDone, GetProductDone}
import com.dabanshan.products.impl.category.CategoryCommand.{CreateCategory, GetAllCategory}
import com.dabanshan.products.impl.category.CategoryEntity
import com.dabanshan.products.impl.product.{ProductEntity, ProductRepository}
import com.dabanshan.user.api.UserCommand.{CreateUser, GetUser}
import com.dabanshan.user.api.UserEntity
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry

import scala.concurrent.ExecutionContext

/**
  * Created by skylai on 2017/9/30.
  */
class ProductServiceImpl (persistentEntityRegistry: PersistentEntityRegistry,
                          userRepository: ProductRepository)(implicit ec: ExecutionContext) extends ProductService{
  /**
    * 创建商品
    *
    * @return
    */
  override def createProduct: ServiceCall[ProductCreation, CreationProductDone] = ???

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
    val ref = catagoryEntityRef(Id().randomID.toString)
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
  override def getProduct(): ServiceCall[NotUsed, GetProductDone] = ???



  private def catagoryEntityRef(id: String) = persistentEntityRegistry.refFor[CategoryEntity](id.toString)

  private def productEntityRef(id: String) = persistentEntityRegistry.refFor[ProductEntity](id.toString)

}
