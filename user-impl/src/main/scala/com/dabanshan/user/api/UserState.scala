package com.dabanshan.user.api

import play.api.libs.json.{Format, Json}

/**
  * Created by skylai on 2017/9/4.
  */

case class User(
                 userId: String,
                 firstName: String,
                 lastName: String,
                 email: String,
                 username: String,
                 password: String
               )
object User {
  implicit val format: Format[User] = Json.format
}