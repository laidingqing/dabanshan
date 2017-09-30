package com.dabanshan.products.api

import akka.{Done, NotUsed}
import com.dabanshan.catalog.api.ProductService
import com.dabanshan.product.api.model.request.ProductCreation
import com.dabanshan.product.api.model.response.{CreationProductDone, GetProductDone}
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
override def findProductByCategory(categoryId: String): ServiceCall[NotUsed, Seq[GetProductDone]] = ???

  /**
    * 根据编号获取商品信息
    *
    * @return
    */
  override def getProduct(): ServiceCall[NotUsed, GetProductDone] = ???
}
