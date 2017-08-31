package com.dabanshan.user.api

import com.lightbend.lagom.scaladsl.persistence.PersistentEntity
import com.dabanshan.user.api._
/**
  * Created by skylai on 2017/8/30.
  */
class UserEntity extends PersistentEntity {

  override type Command = UserCommand
  override type Event = UserEvent
  override type State = Option[User]
  override def initialState = None

  override def behavior: Behavior = {
    case Some(user) =>
      Actions().onReadOnlyCommand[GetUser.type, Option[User]] {
        case (GetUser(userId), ctx, state) => ctx.reply(state)
      }
  }
}
