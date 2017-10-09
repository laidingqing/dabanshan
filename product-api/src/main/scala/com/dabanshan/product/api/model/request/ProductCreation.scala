package com.dabanshan.product.api.model.request

import play.api.libs.json.{Format, Json}
import com.wix.accord.dsl._
/**
  * Created by skylai on 2017/9/4.
  */
case class ProductCreation(
      id: String,
      name: String,
      price: BigDecimal,
      unit: String,
      description: Option[String],
      thumbnails: Option[Seq[String]],
      details: Option[Seq[String]]
)

object ProductCreation {
  implicit val format: Format[ProductCreation] = Json.format

  implicit val productCreationValidator = validator[ProductCreation] { u =>

  }
}