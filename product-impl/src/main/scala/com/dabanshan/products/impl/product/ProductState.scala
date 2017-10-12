package com.dabanshan.products.impl.product

import play.api.libs.json.{Format, Json}
import com.dabanshan.commons.utils.JsonFormats._
import com.dabanshan.product.api.model.ProductStatus
import play.api.data._
import play.api.data.Forms._


case class Product(
  id: String,
  name: String,
  price: BigDecimal,
  unit: String,
  category: String,
  description: Option[String],
  status: ProductStatus.Status,
  thumbnails: Option[List[String]],
  details: Option[List[String]],
  creator: String
){

  def updatePrice(price: Double) = {
    copy(
      price = price
    )
  }

  def appendThumbnails(ids: List[String]) = {
    var thumbs  = thumbnails.getOrElse(List()) ::: ids
    copy(
      thumbnails = Some(thumbs)
    )
  }

  def cancel = {
    copy(
      status = ProductStatus.Cancelled
    )
  }

}

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
