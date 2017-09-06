package com.dabanshan.user.api

import java.util.UUID

import akka.{Done, NotUsed}
import com.dabanshan.catalog.api.UserService
import com.dabanshan.commons.identity.Id
import com.dabanshan.commons.response.GeneratedIdDone
import com.dabanshan.user.api.UserCommand.{CreateUser, GetUser}
import com.dabanshan.user.api.model.request.{UserCreation, WithUserCreationFields}
import com.dabanshan.user.api.model.response.{CreationUserDone, GetUserDone}
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry

import scala.concurrent.{ExecutionContext, Future}

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
  override def registration(): ServiceCall[UserCreation, CreationUserDone] = ServiceCall { request =>
    def executeCommandCallback = () => {
      val ref = persistentEntityRegistry.refFor[UserEntity](Id().randomID.toString)
      ref.ask(
        CreateUser(
          firstName = request.firstName,
          lastName = request.lastName,
          email = request.email,
          username = request.username,
          password = request.password
        )
      )
    }
    reserveUsernameAndEmail(request, executeCommandCallback)
  }

  private def reserveUsernameAndEmail[B](request: WithUserCreationFields, onSuccess: () => Future[B]): Future[B] = {
    onSuccess.apply()
  }

  /**
    * 获取账户信息
    *
    * @param userId
    * @return
    */
  override def getUser(userId: String): ServiceCall[NotUsed, GetUserDone] = ServiceCall{ _ =>
    val ref = persistentEntityRegistry.refFor[UserEntity](userId)
    ref.ask(GetUser)
  }
}
