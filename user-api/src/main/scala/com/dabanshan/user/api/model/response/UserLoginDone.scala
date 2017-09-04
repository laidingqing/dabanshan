package com.dabanshan.user.api.model.response

import play.api.libs.json.{Format, Json}

/**
  * Created by skylai on 2017/9/4.
  */
case class UserLoginDone(
  authToken: String,
  refreshToken: String
)

object UserLoginDone {
  implicit val format: Format[UserLoginDone] = Json.format
}