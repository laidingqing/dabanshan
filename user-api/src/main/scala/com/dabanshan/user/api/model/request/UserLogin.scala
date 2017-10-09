package com.dabanshan.user.api.model.request

/**
  * Created by skylai on 2017/9/4.
  */
import play.api.libs.json.{Format, Json}
import com.wix.accord.dsl._

case class UserLogin(
  username: String,
  password: String
)

object UserLogin {
  implicit val format: Format[UserLogin] = Json.format

  implicit val userLoginValidation = validator[UserLogin] { u =>
    u.username as "username.notEmpty" is notEmpty
    u.password as "password.notEmpty" is notEmpty
  }
}