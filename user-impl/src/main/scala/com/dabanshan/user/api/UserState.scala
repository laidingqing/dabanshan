package com.dabanshan.user.api

import java.time.Instant

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

object UserState {
  val empty = UserState(None, created = false)
  implicit val userFormat = Json.format[User]
  implicit val format: Format[UserState] = Json.format
}

final case class UserState(user: Option[User], created: Boolean) {

  def withBody(body: User): UserState = {
    user match {
      case Some(c) =>
        copy(user = Some(c.copy(userId = body.userId, username = body.username, firstName = body.firstName, lastName = body.lastName, email = body.email, password = body.password)))
      case None =>
        throw new IllegalStateException("Can't set body without content")
    }
  }
  def isEmpty: Boolean = user.isEmpty
}


