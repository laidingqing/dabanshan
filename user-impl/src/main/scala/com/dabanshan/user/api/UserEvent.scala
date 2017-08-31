package com.dabanshan.user.api

import java.time.Instant
import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag, AggregateEventTagger}
import play.api.libs.json.{Format, Json}
/**
  * Created by skylai on 2017/8/30.
  */
sealed trait UserEvent extends AggregateEvent[UserEvent] {
  override def aggregateTag: AggregateEventTagger[UserEvent] = UserEvent.Tag
}

object UserEvent {
  val NumShards = 4
  val Tag = AggregateEventTag.sharded[UserEvent](NumShards)
}

case class UserCreated(userId: String, email: String, timestamp: Instant = Instant.now()) extends UserEvent

object UserCreated {
  implicit val format: Format[UserCreated] = Json.format
}
