package com.dabanshan.user.api

import play.api.libs.json.{Format, Json}

/**
  * Created by skylai on 2017/8/31.
  */
case class CreateProfileMessage(bio:String, avatar:String, nickName:String)

object CreateProfileMessage{
  implicit val format: Format[CreateProfileMessage] = Json.format[CreateProfileMessage]
}


case class Profile(uid:String, bio:String, avatar:String, nickName:String)

object Profile{
  implicit val format: Format[Profile] = Json.format[Profile]
}