package com.dabanshan.product.api.model.response

import com.dabanshan.commons.response.GeneratedIdDone
import play.api.libs.json.{Format, Json}

/**
  * Created by skylai on 2017/9/4.
  */
case class CreationProductDone(
  id: String
) extends GeneratedIdDone

object CreationProductDone {
  implicit val format: Format[CreationProductDone] = Json.format
}