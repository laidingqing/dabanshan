package com.dabanshan.order.api.model.response

import com.dabanshan.commons.response.GeneratedIdDone
import play.api.libs.json.{Format, Json}

/**
  * Created by skylai on 2017/10/1.
  */
case class CreationOrderDone(
                             id: String
                           ) extends GeneratedIdDone

object CreationOrderDone {
  implicit val format: Format[CreationOrderDone] = Json.format
}
