package com.dabanshan.products.impl.category

import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag}
import play.api.libs.json.{Format, Json}

/**
  * Created by skylai on 2017/9/30.
  */

sealed trait CategoryEvent extends AggregateEvent[CategoryEvent] {
  override def aggregateTag = CategoryEvent.Tag
}

object CategoryEvent {
  val NumShards = 4
  val Tag = AggregateEventTag.sharded[CategoryEvent](NumShards)
}

case class CategoryCreated(
  id: String,
  name: String
) extends CategoryEvent

object CategoryCreated {
  implicit val format: Format[CategoryCreated] = Json.format
}

case class CategoryWithProductCreated(
    id: String,
    productId: String
) extends CategoryEvent

object CategoryWithProductCreated {
  implicit val format: Format[CategoryWithProductCreated] = Json.format
}

case class NameUpdated(name: String) extends CategoryEvent

object NameUpdated {
  implicit val format: Format[NameUpdated] = Json.format
}
