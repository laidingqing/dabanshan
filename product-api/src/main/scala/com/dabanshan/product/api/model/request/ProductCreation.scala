package com.dabanshan.product.api.model.request

import play.api.libs.json.{Format, Json}
import com.wix.accord.dsl._
/**
  * Created by skylai on 2017/9/4.
  */
case class ProductCreation(
      id: String,
      name: String,
      description: Option[String],
      thumbnails: Seq[String],
      price: BigDecimal,
      unit: String,
      details: Seq[String]
)

object ProductCreation {
  implicit val format: Format[ProductCreation] = Json.format

  implicit val productCreationValidator = validator[ProductCreation] { u =>

  }
}