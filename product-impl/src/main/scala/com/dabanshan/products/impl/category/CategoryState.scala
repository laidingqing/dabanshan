package com.dabanshan.products.impl.category

import play.api.libs.json.{Format, Json}

/**
  * Created by skylai on 2017/9/30.
  */
case class Category(
    id: String,
    name: String
){
  def updateName(newName: String) = {
    copy(
      name = newName
    )
  }
}

object Category {
  implicit val format: Format[Category] = Json.format
}

object CategoryState {
  val empty = CategoryState(None, created = false)
  implicit val categoryFormat = Json.format[Category]
  implicit val format: Format[CategoryState] = Json.format
}

final case class CategoryState(category: Option[Category], created: Boolean) {

  def withBody(body: Category): CategoryState = {
    category match {
      case Some(c) =>
        copy(category = Some(c.copy(id = body.id, name = body.name)))
      case None =>
        throw new IllegalStateException("Can't set body without content")
    }
  }
  def isEmpty: Boolean = category.isEmpty
}
