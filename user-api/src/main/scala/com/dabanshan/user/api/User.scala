package com.dabanshan.user.api

import play.api.libs.json.{Format, Json}
/**
  * Created by skylai on 2017/8/30.
  */
case class User(userId:String, email:String, password:String)

object User{
  implicit val format: Format[User] = Json.format[User]
}

case class LoginUser(email:String,userId:String,password:String)

object LoginUser{
  implicit val format: Format[LoginUser] = Json.format[LoginUser]
}

case class ResetPassword(email:String,userId:String,password:String)

object ResetPassword{
  implicit val format: Format[ResetPassword] = Json.format[ResetPassword]
}