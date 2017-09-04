package com.dabanshan.product.api.model

import play.api.libs.json.{Format, Json}

import scala.collection.immutable
/**
  * Created by skylai on 2017/9/1.
  */

/**
  * 分类定义
  * @param id
  * @param name
  * @param description
  * @param parent
  */
case class Catalog(
  id: String,
  name: String,
  description: Option[String],
  parent: Option[String]
)

object Catalog {
  implicit val format: Format[Catalog] = Json.format
}
/**
  * 规格定义
  * @param id
  * @param name
  */
case class Specification(
  id: String,
  name: String
)

object Specification {
  implicit val format: Format[Specification] = Json.format
}
/**
  * 商品
  * @param sku
  * @param name
  * @param description
  * @param thumbnails
  * @param price
  * @param unit
  * @param stock
  * @param details
  */
case class Product(
    sku: String,
    name: String,
    description: Option[String],
    thumbnails: Set[String],
    price: BigDecimal,
    unit: String,
    stock: Integer = 0,
    details: Set[String],
    catalogs: immutable.Seq[Catalog]
)

object Product {
  implicit val format: Format[Product] = Json.format
}
/**
  *
  * @param catalogId
  * @param products
  */
case class CatalogByProduct(
  catalogId: String,
  products: immutable.Seq[String]
)

object CatalogByProduct {
  implicit val format: Format[CatalogByProduct] = Json.format
}

case class ProductByCatalog(
   productId: String,
   catalogs: immutable.Seq[String]
)

object ProductByCatalog {
  implicit val format: Format[ProductByCatalog] = Json.format
}


case class NewCatalog(addedByProductId: String)

object NewCatalog {
  implicit val format: Format[NewCatalog] = Json.format
}
