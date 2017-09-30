package com.dabanshan.commons.response

import play.api.libs.json.{Format, Json}

/**
  * Created by skylai on 2017/9/7.
  */
case class TokenContent(userId: String, username: String, isRefreshToken: Boolean = false)

object TokenContent {
  implicit val format: Format[TokenContent] = Json.format
}