package com.dabanshan.commons.response

import play.api.libs.json.{Format, Json}

/**
  * Created by skylai on 2017/9/4.
  */
case class GeneratedIdDone(id: String)

object GeneratedIdDone {
  implicit val format: Format[GeneratedIdDone] = Json.format
}
