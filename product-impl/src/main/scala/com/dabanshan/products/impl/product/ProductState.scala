package com.dabanshan.products.impl.product

import play.api.libs.json.{Format, Json}

/**
  * Created by skylai on 2017/9/30.
  */
case class Product(
  id: String
)

object Product {
  implicit val format: Format[Product] = Json.format
}

object ProductState {
  val empty = ProductState(None, created = false)
  implicit val productFormat = Json.format[Product]
  implicit val format: Format[ProductState] = Json.format
}

final case class ProductState(product: Option[Product], created: Boolean) {

  def withBody(body: Product): ProductState = {
    product match {
      case Some(c) =>
        copy(product = Some(c.copy(id = body.id)))
      case None =>
        throw new IllegalStateException("Can't set body without content")
    }
  }
  def isEmpty: Boolean = product.isEmpty
}
