package com.dabanshan.user.api

import java.time.Instant

import akka.Done
import com.dabanshan.commons.identity.Id
import com.dabanshan.commons.response.GeneratedIdDone
import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag, PersistentEntity}
import com.dabanshan.user.api._
import play.api.libs.json.{Format, Json}
import com.dabanshan.commons.utils.JsonFormats._
import com.dabanshan.commons.utils.SecurePasswordHashing
import com.dabanshan.user.api.UserCommand.{CreateTenant, CreateUser, GetUser}
import com.dabanshan.user.api.model.response.{CreationTenantDone, CreationUserDone, GetUserDone}
import com.lightbend.lagom.scaladsl.api.transport.NotFound

/**
  * Created by skylai on 2017/8/30.
  */
class UserEntity extends PersistentEntity {

  override type Command = UserCommand[_]
  override type Event = UserEvent
  override type State = UserState

  override def initialState: UserState = UserState(None)

  override def behavior: Behavior = {
    case UserState(maybe, _) => Actions().onCommand[CreateUser, CreationUserDone] {
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
    }.onCommand[CreateTenant, CreationTenantDone]{
      case (CreateTenant(tenant), ctx, state) =>
        ctx.thenPersist(TenantCreated(tenant)){_=> ctx.reply(CreationTenantDone(""))} //todo
    }.onReadOnlyCommand[GetUser.type, GetUserDone] {
      case (GetUser, ctx, state) =>
        ctx.reply(GetUserDone(
          userId = state.user.get.userId,
          firstName = state.user.get.firstName,
          lastName = state.user.get.lastName,
          email = state.user.get.email,
          username = state.user.get.username
        ))
    }.onEvent {
      case (UserCreated(userId, firstName, lastName, email, username, hashedPassword), state) =>
        UserState(Some(User(
          userId = userId,
          firstName = firstName,
          lastName = lastName,
          email = email,
          username = username,
          password = hashedPassword
        )))
    }
  }

}
