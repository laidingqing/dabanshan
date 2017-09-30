package com.dabanshan.product.api.model.request

import play.api.libs.json.{Format, Json}
import com.wix.accord.dsl._
/**
  * Created by skylai on 2017/9/30.
  */
case class CategoryCreation (
  id: String,
  name: String,
  description: Option[String],
  parent: Option[String]
)

object CategoryCreation {
  implicit val format: Format[CategoryCreation] = Json.format

  implicit val categoryCreationValidator = validator[CategoryCreation] { u =>

  }
}