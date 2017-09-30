package com.dabanshan.user.api.model.response

import play.api.libs.json.{Format, Json}

/**
  * Created by skylai on 2017/9/7.
  */
case class LoginUserDone(
                          authToken: String,
                          refreshToken: String
                        )

object LoginUserDone {
  implicit val format: Format[LoginUserDone] = Json.format
}