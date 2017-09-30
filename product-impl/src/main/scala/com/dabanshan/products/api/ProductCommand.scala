package com.dabanshan.products.api

import com.dabanshan.product.api.model.response.{CreationProductDone, GetProductDone}
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import com.lightbend.lagom.scaladsl.playjson.JsonSerializer
import com.lightbend.lagom.scaladsl.playjson.JsonSerializer._

/**
  * Created by skylai on 2017/9/30.
  */
sealed trait ProductCommand[R] extends ReplyType[R]

object ProductCommand {

  import play.api.libs.json._
  import JsonSerializer.emptySingletonFormat

  val serializers = Vector(
    JsonSerializer(Json.format[CreateProduct]),
    JsonSerializer(emptySingletonFormat(GetProduct)))

  case class CreateProduct(

   ) extends ProductCommand[CreationProductDone]

  case object GetProduct extends ProductCommand[GetProductDone]

}