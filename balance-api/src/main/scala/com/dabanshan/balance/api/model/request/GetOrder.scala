package com.dabanshan.balance.api.model.request

import com.wix.accord.dsl._
import play.api.libs.json.{Format, Json}

/**
  * Created by skylai on 2017/10/5.
  */

case class GetOrder(id: String)

object GetOrder {
  implicit val format: Format[GetOrder] = Json.format
  implicit val orderCreationValidator = validator[GetOrder] { u =>

  }
}