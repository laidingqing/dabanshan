package com.dabanshan.product.api.model.response

import com.dabanshan.commons.response.GeneratedIdDone
import play.api.libs.json.{Format, Json}

/**
  * Created by skylai on 2017/9/30.
  */
case class CreationCategoryDone(
  id: String
) extends GeneratedIdDone

object CreationCategoryDone {
  implicit val format: Format[CreationCategoryDone] = Json.format
}