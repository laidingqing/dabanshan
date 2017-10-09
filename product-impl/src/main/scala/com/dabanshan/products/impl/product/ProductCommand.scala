package com.dabanshan.products.impl.product

import com.dabanshan.product.api.model.response.{CreationProductDone, GetProductDone}
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import com.lightbend.lagom.scaladsl.playjson.JsonSerializer

/**
  * Created by skylai on 2017/9/30.
  */
sealed trait ProductCommand[R] extends ReplyType[R]

object ProductCommand {

  import JsonSerializer.emptySingletonFormat
  import play.api.libs.json._

  val serializers = Vector(
    JsonSerializer(Json.format[CreateProduct]),
    JsonSerializer(emptySingletonFormat(GetProduct)))

  case class CreateProduct(
      id: String
   ) extends ProductCommand[CreationProductDone]

  case object GetProduct extends ProductCommand[GetProductDone]

}