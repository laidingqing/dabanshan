package com.dabanshan.user.api

import java.time.Instant

import com.dabanshan.user.api.model.TenantStatus
import play.api.libs.json.{Format, Json}

/**
  * Created by skylai on 2017/9/4.
  */
case class Tenant(
   name: String,
   userId: String,
   address: Option[String],
   phone: Option[String],
   province: Option[String],
   city: Option[String],
   county: Option[String],
   description: Option[String],
   credentials: Option[List[String]],
   status: TenantStatus.Status
 )
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
  implicit val userFormat = Json.format[User]
  implicit val format: Format[UserState] = Json.format
}

final case class UserState(user: Option[User], timestamp: Instant = Instant.now()) {

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


