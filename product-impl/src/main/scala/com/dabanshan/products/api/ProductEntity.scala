package com.dabanshan.products.api

import com.dabanshan.commons.utils.SecurePasswordHashing
import com.dabanshan.product.api.model.response.{CreationProductDone, GetProductDone}
import com.dabanshan.products.api.ProductCommand.{CreateProduct, GetProduct}
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity

/**
  * Created by skylai on 2017/9/30.
  */
class ProductEntity extends PersistentEntity {
  override type Command = ProductCommand[_]
  override type Event = ProductEvent
  override type State = ProductState

  override def initialState: ProductState = ProductState.empty

  override def behavior: Behavior = {
    case state if state.isEmpty => initial
    case state if !state.isEmpty => productCreated
  }


  private val initial: Actions = {
    Actions()
      .onCommand[CreateProduct, CreationProductDone] {
      case (CreateProduct(), ctx, state) =>
        ctx.thenPersist(
          ProductCreated(
            id = entityId
          )
        ) { _ =>
          ctx.reply(CreationProductDone(id = entityId))
        }

    }
      .onEvent {
        case (ProductCreated(id), state) =>
          ProductState(Some(Product(
            id = id
          )), created = true)
      }
  }

  private val productCreated: Actions = {
    Actions()
      .onReadOnlyCommand[GetProduct.type, GetProductDone] {
      case (GetProduct, ctx, state) =>
        ctx.reply(GetProductDone("", "", Some(""), Seq.empty, 0, "", Seq.empty))
    }
  }
}
