package com.dabanshan.balance.api.model.response

import java.util.Date

import com.dabanshan.commons.response.GeneratedIdDone
import play.api.libs.json.{Format, Json}

/**
  * Created by skylai on 2017/10/5.
  */
case class GetOrderItem(id: String, name: String, price: BigDecimal)

case class GetOrderDone(
  id: String,
  createdAt: Date,
  amount: BigDecimal,
  status: Integer,
  items: Seq[GetOrderItem]
) extends GeneratedIdDone

object GetOrderDone {
  implicit val format: Format[GetOrderDone] = Json.format
}
