package com.dabanshan.user.api.model.response

import play.api.libs.json.{Format, Json}

/**
  * Created by skylai on 2017/9/4.
  */
case class GetUserDone(
   userId: String,
   firstName: String,
   lastName: String,
   email: String,
   username: String
)
object GetUserDone {
  implicit val format: Format[GetUserDone] = Json.format
}