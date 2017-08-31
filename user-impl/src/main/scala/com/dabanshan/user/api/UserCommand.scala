package com.dabanshan.user.api

import akka.NotUsed
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import play.api.libs.json.{Format, Json}


/**
  * Created by skylai on 2017/8/30.
  */
sealed trait UserCommand[R] extends ReplyType[R]

case class CreateUser(userId: String, email: String) extends UserCommand[User]

object CreateUser {
  implicit val format: Format[CreateUser] = Json.format
}


case class GetUser(userId: String) extends UserCommand[User]

object GetUser {
  implicit val format: Format[GetUser] = Json.format
}