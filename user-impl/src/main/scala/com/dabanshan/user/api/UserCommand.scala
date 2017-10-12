package com.dabanshan.user.api

import com.dabanshan.commons.response.GeneratedIdDone
import com.dabanshan.user.api.model.response.{CreationTenantDone, CreationUserDone, GetUserDone}
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import play.api.libs.json.{Format, Json}
import com.dabanshan.commons.utils.JsonFormats._
import com.lightbend.lagom.scaladsl.playjson.JsonSerializer
/**
  * Created by skylai on 2017/9/4.
  */
//Command definition

sealed trait UserCommand[R] extends ReplyType[R]

object UserCommand {

  import play.api.libs.json._
  import JsonSerializer.emptySingletonFormat

  val serializers = Vector(
    JsonSerializer(Json.format[CreateUser]),
    JsonSerializer(Json.format[CreationUserDone]),
    JsonSerializer(Json.format[CreateTenant]),
    JsonSerializer(emptySingletonFormat(GetUser)))

  case class CreateUser(
                         firstName: String,
                         lastName: String,
                         email: String,
                         username: String,
                         password: String
                       ) extends UserCommand[CreationUserDone]

  case object GetUser extends UserCommand[GetUserDone]

  case class CreateTenant(tenant: Tenant) extends UserCommand[CreationTenantDone]


}