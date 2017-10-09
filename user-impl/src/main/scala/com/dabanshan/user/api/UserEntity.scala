package com.dabanshan.user.api

import java.time.Instant

import akka.Done
import com.dabanshan.commons.identity.Id
import com.dabanshan.commons.response.GeneratedIdDone
import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag, PersistentEntity}
import com.dabanshan.user.api._
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import play.api.libs.json.{Format, Json}
import com.dabanshan.commons.utils.JsonFormats._
import com.dabanshan.commons.utils.SecurePasswordHashing
import com.dabanshan.user.api.UserCommand.{CreateUser, GetUser}
import com.dabanshan.user.api.model.response.{CreationUserDone, GetUserDone}
import com.lightbend.lagom.scaladsl.api.transport.NotFound

/**
  * Created by skylai on 2017/8/30.
  */
class UserEntity extends PersistentEntity {

  override type Command = UserCommand[_]
  override type Event = UserEvent
  override type State = UserState

  override def initialState: UserState = UserState.empty

  override def behavior: Behavior = {
    case state if state.isEmpty => initial
    case state if !state.isEmpty => userCreated
  }

  private val initial: Actions = {
    Actions()
      .onCommand[CreateUser, CreationUserDone] {
      case (CreateUser(firstName, lastName, email, username, password), ctx, state) =>
        val hashedPassword = SecurePasswordHashing.hashPassword(password)
        ctx.thenPersist(
          UserCreated(
            userId = entityId,
            firstName = firstName,
            lastName = lastName,
            email = email,
            username = username,
            hashedPassword = hashedPassword
          )
        ) { _ =>
          ctx.reply(CreationUserDone(id = entityId))
        }

    }
      .onEvent {
        case (UserCreated(userId, firstName, lastName, email, username, hashedPassword), state) =>
          UserState(Some(User(
            userId = userId,
            firstName = firstName,
            lastName = lastName,
            email = email,
            username = username,
            password = hashedPassword
          )), created = true)
      }
  }

  private val userCreated: Actions = {
    Actions()
      .onReadOnlyCommand[GetUser.type, GetUserDone] {
      case (GetUser, ctx, state) =>
        ctx.reply(GetUserDone(
          userId = state.user.get.userId,
          firstName = state.user.get.firstName,
          lastName = state.user.get.lastName,
          email = state.user.get.email,
          username = state.user.get.username
        ))
    }
  }

}
