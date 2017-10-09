package com.dabanshan.product.api.model.response

import java.util.UUID

import play.api.libs.json.{Format, Json}

/**
  * Created by skylai on 2017/9/30.
  */
case class GetAllCategoryDone(
  id: String,
  name: String
)

object GetAllCategoryDone {
  implicit val format: Format[GetAllCategoryDone] = Json.format
}