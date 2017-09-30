package com.dabanshan.products.impl.category

import akka.Done
import com.dabanshan.product.api.model.response.{CreationCategoryDone, GetAllCategoryDone}
import com.dabanshan.products.impl.category.CategoryCommand.{CreateCategory, GetAllCategory, UpdateName}
import com.dabanshan.products.impl.product.ProductCreated
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity

/**
  * Created by skylai on 2017/9/30.
  */
class CategoryEntity extends PersistentEntity {
  override type Command = CategoryCommand[_]
  override type Event = CategoryEvent
  override type State = CategoryState

  override def initialState: CategoryState = CategoryState.empty

  override def behavior: Behavior = {
    case state if state.isEmpty => initial
    case state if !state.isEmpty => categoryCreated
  }

  private val initial: Actions = {
    Actions()
      .onCommand[CreateCategory, CreationCategoryDone] {
      case (CreateCategory(name), ctx, state) =>
        ctx.thenPersist(
          CategoryCreated(
            id = entityId,
            name = name
          )
        ) { _ =>
          ctx.reply(CreationCategoryDone(id = entityId))
        }

    }.onEvent {
        case (CategoryCreated(id, name), state) =>
          CategoryState(Some(Category(
            id = id,
            name = name
          )), created = true)
      }
  }

  private val categoryCreated: Actions = {
    Actions()
      .onReadOnlyCommand[GetAllCategory.type, GetAllCategoryDone] {
      case (GetAllCategory, ctx, state) =>
        ctx.reply(
          GetAllCategoryDone(
            id = state.category.get.id,
            name = state.category.get.name
          )
        )
    }.onCommand[UpdateName, Done]{
      case (UpdateName(name), ctx, state) =>
        ctx.thenPersist(NameUpdated(name))(_ => ctx.reply(Done))
    }.onEvent{
      case (NameUpdated(name), state) => CategoryState(Some(state.category.get.updateName(name)), created = true)
    }
  }
}
