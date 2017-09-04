package com.dabanshan.user.api

import play.api.libs.json.{Format, Json}

/**
  * Created by skylai on 2017/9/4.
  */
//State definition
case class UserState(user: Option[User])

object UserState {
  implicit val format: Format[UserState] = Json.format
}

case class User(
                 id: String,
                 firstName: String,
                 lastName: String,
                 email: String,
                 username: String,
                 password: String
               )
object User {
  implicit val format: Format[User] = Json.format
}