package com.dabanshan.product.api.model

import com.dabanshan.commons.utils.JsonFormats._
import play.api.libs.json.Format

/**
  * Created by skylai on 2017/10/11.
  */
object ProductStatus extends Enumeration {
  val Created, Cancelled = Value
  type Status = Value

  implicit val format: Format[Status] = enumFormat(ProductStatus)
}
