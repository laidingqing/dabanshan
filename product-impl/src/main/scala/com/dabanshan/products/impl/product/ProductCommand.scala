package com.dabanshan.products.impl.product

import akka.Done
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

  //创建商品命令
  case class CreateProduct(product: Product) extends ProductCommand[CreationProductDone]
  //获取商品命令
  case object GetProduct extends ProductCommand[GetProductDone]
  //更新价格命令
  case class UpdatePrice(price: BigDecimal) extends ProductCommand[Done]

  object UpdatePrice {
    implicit val format: Format[UpdatePrice] = Json.format
  }

  case class AddThumbnails(ids: List[String]) extends ProductCommand[Done]

  object AddThumbnails{
    implicit val format: Format[AddThumbnails] = Json.format
  }

}