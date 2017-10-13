package com.dabanshan.user.api

import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag}
import play.api.libs.json.{Format, Json}

/**
  * Created by skylai on 2017/9/4.
  */
//Event definition
sealed trait UserEvent extends AggregateEvent[UserEvent] {
  override def aggregateTag = UserEvent.Tag
}

object UserEvent {
  val NumShards = 4
  val Tag = AggregateEventTag.sharded[UserEvent](NumShards)
}

case class UserCreated(userId: String, username: String, email: String, firstName: String, lastName: String,  hashedPassword: String) extends UserEvent

object UserCreated {
  implicit val format: Format[UserCreated] = Json.format
}

case class TenantCreated(tenant: Tenant) extends UserEvent

object TenantCreated {
  implicit val format: Format[TenantCreated] = Json.format
}