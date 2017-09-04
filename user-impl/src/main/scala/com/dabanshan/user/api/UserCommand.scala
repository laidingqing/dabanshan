package com.dabanshan.user.api

import com.dabanshan.commons.response.GeneratedIdDone
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import play.api.libs.json.{Format, Json}

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
