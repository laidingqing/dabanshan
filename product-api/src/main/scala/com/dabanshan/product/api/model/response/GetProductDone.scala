package com.dabanshan.product.api.model.response

import com.dabanshan.product.api.model.ProductStatus
import play.api.libs.json.{Format, Json}

/**
  * Created by skylai on 2017/9/4.
  */
case class GetProductDone(
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
)

object GetProductDone {
  implicit val format: Format[GetProductDone] = Json.format
}


