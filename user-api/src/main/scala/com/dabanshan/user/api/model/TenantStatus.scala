package com.dabanshan.user.api.model

import com.dabanshan.commons.utils.JsonFormats._
import play.api.libs.json.Format

/**
  * Created by skylai on 2017/10/13.
  */


object TenantStatus extends Enumeration {
  val Created, Ok, Cancelled = Value
  type Status = Value

  implicit val format: Format[Status] = enumFormat(TenantStatus)
}
