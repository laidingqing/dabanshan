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
import com.dabanshan.user.api.model.response.UserDone
import com.lightbend.lagom.scaladsl.api.transport.NotFound

/**
  * Created by skylai on 2017/8/30.
  */
class UserEntity extends PersistentEntity {

  override type Command = UserCommand[_]
  override type Event = UserEvent
  override type State = Option[User]

  override def initialState: Option[User] = None

  override def behavior: Behavior = {
    case None => notCreated
    case Some(user) => {
      System.out.println("user :" + user)
      Actions().onReadOnlyCommand[GetUser.type , UserDone] {
        case (GetUser, ctx, state) =>
          ctx.reply(UserDone("", "", "", "", ""))
      }
    }
  }

  private val notCreated = {
    Actions().onCommand[CreateUser, GeneratedIdDone] {
      case (CreateUser(firstName, lastName, email, username, password), ctx, state) =>
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
    }.onEvent {
      case (UserCreated(userId, firstName, lastName, email, username, password), state) =>
        System.out.println("created state:" + state)
        Some(User(
        userId = userId,
        firstName = firstName,
        lastName = lastName,
        email = email,
        username = username,
        password = password
      ))
    }
  }

}
