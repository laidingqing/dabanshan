package com.dabanshan.products.impl.product

import com.dabanshan.products.impl.category.CategoryCreated
import com.dabanshan.products.impl.product.ProductCommand.GetProduct
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}

import scala.collection.immutable

/**
  * Created by skylai on 2017/9/30.
  */
object ProductsSerializerRegistry extends JsonSerializerRegistry {
  override def serializers: immutable.Seq[JsonSerializer[_]] = immutable.Seq(
    JsonSerializer[ProductCreated],
    JsonSerializer[CategoryCreated],
    JsonSerializer[GetProduct.type ],
    JsonSerializer[ProductThumbnailsCreated]
  )
}