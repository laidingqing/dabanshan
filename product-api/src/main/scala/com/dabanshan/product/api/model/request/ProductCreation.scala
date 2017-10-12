package com.dabanshan.product.api.model.request

import play.api.libs.json.{Format, Json}
import com.wix.accord.dsl._
/**
  * Created by skylai on 2017/9/4.
  */
case class ProductCreation(
      name: String,
      price: BigDecimal,
      unit: String,
      category: String,
      description: Option[String],
      thumbnails: Option[List[String]],
      details: Option[List[String]],
      creator: String
)

object ProductCreation {
  implicit val format: Format[ProductCreation] = Json.format
}