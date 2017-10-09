package com.dabanshan.product.api.model.response

import play.api.libs.json.{Format, Json}

/**
  * Created by skylai on 2017/9/4.
  */
case class GetProductDone(
     id: String,
     name: String,
     description: Option[String],
     thumbnails: Seq[String],
     price: BigDecimal,
     unit: String,
     details: Seq[String]
)

object GetProductDone {
  implicit val format: Format[GetProductDone] = Json.format
}