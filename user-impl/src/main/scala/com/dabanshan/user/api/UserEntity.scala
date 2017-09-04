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
    Actions()
      .onCommand[CreateUser, GeneratedIdDone] {
      case (CreateUser(firstName, lastName, email, username, password), ctx, state) =>
        state.user match {
          case Some(_) =>
            val hashedPassword = SecurePasswordHashing.hashPassword(password)
            val userId = Id().randomID.toString

            ctx.thenPersist(
              UserCreated(
                userId = userId,
                firstName = firstName,
                lastName = lastName,
                email = email,
                username = username,
                hashedPassword = hashedPassword
              )
            ) { _ =>
              ctx.reply(GeneratedIdDone(userId.toString))
            }
          case None =>
            ctx.invalidCommand(s"Client with id ${entityId} not found")
            ctx.done
        }
    }
  }

}
