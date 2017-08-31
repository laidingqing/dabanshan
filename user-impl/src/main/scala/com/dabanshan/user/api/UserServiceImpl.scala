package com.dabanshan.user.api

import akka.{Done, NotUsed}
import com.dabanshan.catalog.api.UserService
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry

import scala.concurrent.ExecutionContext

/**
  * Created by skylai on 2017/8/30.
  */
class UserServiceImpl (persistentEntityRegistry: PersistentEntityRegistry,
                       userRepository: UserRepository)(implicit ec: ExecutionContext) extends UserService{
  override def registration: ServiceCall[User, Done] = ???

  override def getUser(userId: String): ServiceCall[NotUsed, User] = ServiceCall { _ =>
    val ref = persistentEntityRegistry.refFor[UserEntity](userId)
    ref.ask(GetUser(userId))
  }
}
