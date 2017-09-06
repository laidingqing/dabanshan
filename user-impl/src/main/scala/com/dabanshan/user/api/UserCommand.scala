package com.dabanshan.user.api

import com.dabanshan.commons.response.GeneratedIdDone
import com.dabanshan.user.api.model.response.UserDone
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import play.api.libs.json.{Format, Json}
import com.dabanshan.commons.utils.JsonFormats._
/**
  * Created by skylai on 2017/9/4.
  */
//Command definition

sealed trait UserCommand[R] extends ReplyType[R]

case class CreateUser(
                       firstName: String,
                       lastName: String,
                       email: String,
                       username: String,
                       password: String
                     ) extends UserCommand[GeneratedIdDone]

object CreateUser {
  implicit val format: Format[CreateUser] = Json.format
}

case object GetUser extends UserCommand[UserDone] {
  implicit val format: Format[GetUser.type] = singletonFormat(GetUser)
}