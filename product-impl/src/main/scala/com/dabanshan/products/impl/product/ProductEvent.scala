package com.dabanshan.products.impl.product

import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag}
import play.api.libs.json.{Format, Json}

/**
  * Created by skylai on 2017/9/30.
  */
sealed trait ProductEvent extends AggregateEvent[ProductEvent] {
  override def aggregateTag = ProductEvent.Tag
}

object ProductEvent {
  val NumShards = 4
  val Tag = AggregateEventTag.sharded[ProductEvent](NumShards)
}

case class ProductCreated(product: Product) extends ProductEvent

object ProductCreated {
  implicit val format: Format[ProductCreated] = Json.format
}

case class ProductThumbnailsCreated(list: List[String]) extends ProductEvent

object ProductThumbnailsCreated {
  implicit val format: Format[ProductThumbnailsCreated] = Json.format
}