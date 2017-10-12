package com.dabanshan.products.impl.product

import akka.Done
import com.dabanshan.product.api.model.response.{CreationProductDone, GetProductDone}
import com.dabanshan.products.impl.product.ProductCommand.{AddThumbnails, CreateProduct, GetProduct}
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity
import org.slf4j.{Logger, LoggerFactory}

/**
  * Created by skylai on 2017/9/30.
  */
class ProductEntity extends PersistentEntity {

  val log: Logger = LoggerFactory.getLogger(getClass)

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
      case (CreateProduct(product), ctx, state) =>
        ctx.thenPersist(
          ProductCreated(product)
        ) { _ =>
          ctx.reply(CreationProductDone(id = entityId))
        }
    }.onEvent {
        case (ProductCreated(product), state) => ProductState(Some(product), created = true)
      }
  }

  private val productCreated: Actions = {
    Actions()
      .onCommand[AddThumbnails , Done] {
        case (AddThumbnails(ids), ctx, state) =>
          ctx.thenPersist(ProductThumbnailsCreated(ids))(_ => ctx.reply(Done))
      }.onEvent {
        case (ProductThumbnailsCreated(ids), state) =>
          ProductState(Some(state.product.get.appendThumbnails(ids)), created = true)
      }.onReadOnlyCommand[GetProduct.type, GetProductDone] {
        case (GetProduct, ctx, state) =>
          ctx.reply(GetProductDone(
            state.product.get.id,
            state.product.get.name,
            state.product.get.price,
            state.product.get.unit,
            state.product.get.category,
            state.product.get.description,
            state.product.get.status,
            state.product.get.thumbnails,
            state.product.get.details,
            state.product.get.creator
            ))
    }
  }
}