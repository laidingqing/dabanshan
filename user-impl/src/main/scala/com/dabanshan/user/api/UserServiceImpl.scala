package com.dabanshan.user.api

import java.util.UUID

import akka.{Done, NotUsed}
import com.dabanshan.catalog.api.UserService
import com.dabanshan.commons.identity.Id
import com.dabanshan.commons.response.{GeneratedIdDone, TokenContent}
import com.dabanshan.commons.utils.SecurePasswordHashing
import com.dabanshan.user.api.UserCommand.{CreateTenant, CreateUser, GetUser}
import com.dabanshan.user.api.model.TenantStatus
import com.dabanshan.user.api.model.request._
import com.dabanshan.user.api.model.response._
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.transport.{BadRequest, Forbidden}
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

  /**
    * 登录
    *
    * @return
    */
  override def loginUser(): ServiceCall[UserLogin, LoginUserDone] = ServiceCall{ request =>
    def passwordMatches(providedPassword: String, storedHashedPassword: String) = SecurePasswordHashing.validatePassword(providedPassword, storedHashedPassword)

    for {
      maybeUser <- userRepository.findUserByUsername(request.username)
      token = maybeUser.filter(user => passwordMatches(request.password, user.hashedPassword))
        .map(user =>
          TokenContent(
            userId = user.id,
            username = user.username
          )
        )
        .map(tokenContent => TokenUtil.generateTokens(tokenContent))
        .getOrElse(throw Forbidden("Username and password combination not found"))
    }yield {
      LoginUserDone(
        maybeUser.get.id,
        maybeUser.get.username,
        token.authToken,
        token.refreshToken.getOrElse(throw new IllegalStateException("Refresh token missing"))
      )
    }
  }

  // ~ 以下为用户租户信息

  /**
    * 获得用户开通的租户信息
    *
    * @param userId
    * @param tenantId
    * @return
    */
  override def getTenant(userId: String, tenantId: String): ServiceCall[NotUsed, GetTenantDone] = ServiceCall{ _ =>
    for {
      reserved <- userRepository.findTenantByUser(userId)
    }yield{
      reserved match {
        case Some(maybe) => GetTenantDone(
          userId = maybe.userId,
          name = maybe.name,
          address = maybe.address,
          phone = maybe.phone,
          province = maybe.province,
          city = maybe.city,
          county = maybe.county,
          description = maybe.description,
          credentials = maybe.credentials,
          status = maybe.status
        )
        case None => throw BadRequest("No Found Tenant info.")
      }


    }

  }

//  userId: String,
//  name: String,
//  address: Option[String],
//  phone: Option[String],
//  province: Option[String],
//  city: Option[String],
//  county: Option[String],
//  description: Option[String],
//  credentials: Option[List[String]],
//  status: TenantStatus.Status
//
  /**
    * 更新租户信息
    *
    * @param userId
    * @param tenantId
    * @return
    */
override def updateTenant(userId: String, tenantId: String): ServiceCall[TenantUpdation, CreationTenantDone] = ???

  /**
    * 创建用户租户信息
    *
    * @param userId
    * @return
    */
  override def createTenant(userId: String): ServiceCall[TenantCreation, CreationTenantDone] = ServiceCall{ request =>
    def executeAddTenat = () => {
      val ref = persistentEntityRegistry.refFor[UserEntity](userId)
      val tenant = Tenant(
          request.name,
          userId,
          request.address,
          request.phone,
          request.province,
          request.city,
          request.county,
          request.description,
          None,
        TenantStatus.Created
      )
      ref.ask(
        CreateTenant(tenant)
      )
    }
    reserveUserAndTenant(userId, executeAddTenat)
  }

  //检查是否存在user for Tenant Object.
  private def reserveUserAndTenant[B](userId: String, onSuccess: () => Future[B]): Future[B] = {

    val canProceed = for {
      userTenantReserved <- userRepository.findTenantByUser(userId)
    }yield(userTenantReserved)

    canProceed.flatMap(canProceed =>{
      canProceed match {
        case Some(maybe) => throw BadRequest("Either tenant is already taken.")
        case None => onSuccess.apply()
      }
    })
  }
  /**
    * 增加租户资质信息
    *
    * @param userId
    * @param tenantId
    * @return
    */
  override def addTenantCredentials(userId: String, tenantId: String): ServiceCall[NotUsed, Done] = ???
}
