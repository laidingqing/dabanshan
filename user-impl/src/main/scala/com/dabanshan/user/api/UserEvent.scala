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

case class UserCreated(userId: String, firstName: String, lastName: String, email: String, username: String, hashedPassword: String) extends IdentityEvent
object UserCreated {
  implicit val format: Format[UserCreated] = Json.format
}

