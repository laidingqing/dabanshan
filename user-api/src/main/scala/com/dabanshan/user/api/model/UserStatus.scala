package com.dabanshan.user.api.model

import com.dabanshan.commons.utils.JsonFormats._
import play.api.libs.json.Format

/**
  * Created by skylai on 2017/10/13.
  */
object UserStatus extends Enumeration {
  val Created, Locked, Cancelled = Value
  type Status = Value

  implicit val format: Format[Status] = enumFormat(UserStatus)
}
