package com.dabanshan.product.api.model.response

import com.dabanshan.product.api.model.ProductStatus
import play.api.libs.json.{Format, Json}

/**
  * Created by skylai on 2017/10/12.
  */
case class ProductSummary(
                           id: String,
                           name: String,
                           price: BigDecimal,
                           unit: String,
                           category: String,
                           creator: String,
                           status: ProductStatus.Status
                         )

object ProductSummary {
  implicit val format: Format[ProductSummary] = Json.format
}