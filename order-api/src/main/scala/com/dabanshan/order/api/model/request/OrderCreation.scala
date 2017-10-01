package com.dabanshan.order.api.model.request

import com.wix.accord.dsl._
import play.api.libs.json.{Format, Json}

/**
  * Created by skylai on 2017/10/1.
  */
case class OrderCreation(
)

object OrderCreation {
  implicit val format: Format[OrderCreation] = Json.format
  implicit val orderCreationValidator = validator[OrderCreation] { u =>

  }
}