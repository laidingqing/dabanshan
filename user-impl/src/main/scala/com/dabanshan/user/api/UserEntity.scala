package com.dabanshan.user.api

import java.time.Instant

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag, PersistentEntity}
import com.dabanshan.user.api._
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import play.api.libs.json.{Format, Json}
import com.dabanshan.commons.utils.JsonFormats._
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
    case UserState(maybeUser, _) =>
      Actions().onCommand[CreateUser, User]{
        case (CreateUser(userId, email, password), ctx, state) =>
          val newUser = User(userId = userId, email = email, password = password)
          ctx.thenPersist(
            UserCreated(newUser.userId, newUser.email, newUser.password)
          ) { _ =>
            ctx.reply(newUser)
          }
      }.onReadOnlyCommand[GetUser, User] {
        case (GetUser(userId), ctx, UserState(None, _)) =>
          ctx.commandFailed(NotFound(s"No user for id: $userId"))
        case (GetUser(userId), ctx, UserState(Some(withFriends), _)) =>
          ctx.reply(User(userId = userId, email = "withFriends.email", password = ""))
      }.onEvent {
        case (UserCreated(userId, email, _, _), UserState(None, _)) =>
          UserState(Some(User(userId = userId, email = email, password = "")))
      }
  }

}
//Command definition

sealed trait UserCommand[R] extends ReplyType[R]

case class CreateUser(userId: String, email: String, password: String) extends UserCommand[User]
case class GetUser(userId: String) extends UserCommand[User]


object CreateUser {
  implicit val format: Format[CreateUser] = Json.format
}
object GetUser {
  implicit val format: Format[GetUser] = Json.format
}

//Event definition
sealed trait UserEvent extends AggregateEvent[UserEvent] {
  override def aggregateTag = UserEvent.Tag
}

object UserEvent {
  val NumShards = 4
  val Tag = AggregateEventTag.sharded[UserEvent](NumShards)
}

case class UserCreated(userId: String, email: String, password: String, timestamp: Instant = Instant.now()) extends UserEvent

object UserCreated {
  implicit val format: Format[UserCreated] = Json.format
}

object UserStatus extends Enumeration {
  val Created, Completed, Cancelled = Value
  type Status = Value

  implicit val format: Format[Status] = enumFormat(UserStatus)
}

//State definition
case class UserState(user: Option[User], timestamp: Instant = Instant.now())

object UserState {
  implicit val format: Format[UserState] = Json.format
}