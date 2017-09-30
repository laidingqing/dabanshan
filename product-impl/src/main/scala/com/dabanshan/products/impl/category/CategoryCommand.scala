package com.dabanshan.products.impl.category

import akka.Done
import com.dabanshan.product.api.model.response.{CreationCategoryDone, GetAllCategoryDone}
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import com.lightbend.lagom.scaladsl.playjson.JsonSerializer

/**
  * Created by skylai on 2017/9/30.
  */
sealed trait CategoryCommand[R] extends ReplyType[R]

object CategoryCommand {

  import play.api.libs.json._

  val serializers = Vector(
    JsonSerializer(Json.format[CreateCategory])
  )

  /**
    * 创建分类命令
    * @param name
    */
  case class CreateCategory(
    name: String
  ) extends CategoryCommand[CreationCategoryDone]

  /**
    * 更新分类名称
    * @param name 新的名称
    */
  case class UpdateName(name: String) extends CategoryCommand[Done]

  object UpdateName {
    implicit val format: Format[UpdateName] = Json.format
  }

  case object GetAllCategory extends CategoryCommand[GetAllCategoryDone]
}