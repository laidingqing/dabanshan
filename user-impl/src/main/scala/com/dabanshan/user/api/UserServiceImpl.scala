package com.dabanshan.user.api

import java.util.UUID

import akka.{Done, NotUsed}
import com.dabanshan.catalog.api.UserService
import com.dabanshan.commons.identity.Id
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry

import scala.concurrent.ExecutionContext

/**
  * Created by skylai on 2017/8/30.
  */
class UserServiceImpl (persistentEntityRegistry: PersistentEntityRegistry,
                       userRepository: UserRepository)(implicit ec: ExecutionContext) extends UserService{
  /**
    * 注册账号
    *
    * @return
    */
  override def registration: ServiceCall[CreateUserMessage, User] = ServiceCall { userMessage =>
    val userId = Id().randomID.toString
    val ref = persistentEntityRegistry.refFor[UserEntity](userId)
    ref.ask(CreateUser(userId = userId, email = userMessage.email, password = userMessage.password))
  }

  /**
    * 获取用户账号信息
    *
    * @param userId
    * @return
    */
  override def getUser(userId: String): ServiceCall[NotUsed, User] = ServiceCall{ _ =>
    val ref = persistentEntityRegistry.refFor[UserEntity](userId)
    ref.ask(GetUser(userId))
  }
/**
    * 更新用户信息
    *
    * @return
    */
  override def updateProfile: ServiceCall[CreateProfileMessage, Done] = ???

  /**
    * 获取用户信息
    *
    * @return
    */
  override def getProfile: ServiceCall[NotUsed, Profile] = ???
}
