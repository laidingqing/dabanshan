package com.dabanshan.user.api

import akka.Done
import com.dabanshan.user.api.model.TenantStatus

import scala.concurrent.ExecutionContext
import com.datastax.driver.core.{PreparedStatement, TypeCodec}
import com.lightbend.lagom.scaladsl.persistence.ReadSideProcessor
import com.lightbend.lagom.scaladsl.persistence.cassandra.{CassandraReadSide, CassandraSession}

import scala.collection.immutable
import scala.concurrent.{ExecutionContext, Future}
/**
  * Created by skylai on 2017/8/30.
  */
case class UserByUsername(username: String, id: String, hashedPassword: String)


class UserRepository (session: CassandraSession)(implicit ec: ExecutionContext) {

  def findUserByUsername(username: String): Future[Option[UserByUsername]] = {
    val result = session.selectOne("SELECT userId, username, hashed_password FROM users_by_username WHERE username = ?", username).map {
      case Some(row) => Option(
        UserByUsername(
          username = row.getString("username"),
          id = row.getString("userId"),
          hashedPassword = row.getString("hashed_password")
        )
      )
      case None => Option.empty
    }
    result
  }

  /**
    * 查询用户关联的供应商信息
    * @param userId
    * @return
    */
  def findTenantByUser(userId: String): Future[Option[Tenant]] = {
    val result = session.selectOne("SELECT userId, name, phone, address, province, city, county, description FROM tenant_by_user WHERE userId = ?", userId).map {
      case Some(row) => Some(Tenant(
        userId = row.getString("userId"),
        name = row.getString("name"),
        phone = Option(row.getString("phone")),
        address = Option(row.getString("address")),
        province = Option(row.getString("province")),
        city = Option(row.getString("city")),
        county = Option(row.getString("county")),
        description = Option(row.getString("description")),
        credentials = None,//TODO convert row values.
        status = TenantStatus.withName(row.getString("status"))
      ))
      case None => None
    }
    result
  }



}

class UserEventProcessor(session: CassandraSession, readSide: CassandraReadSide)(implicit ec: ExecutionContext)
  extends ReadSideProcessor[UserEvent]
{
  private var insertUserForEmailStatement: PreparedStatement = null
  private var insertUserForTenantStatement: PreparedStatement = null

  override def buildHandler =
    readSide.builder[UserEvent]("userEventOffset")
      .setGlobalPrepare(createTables)
      .setPrepare(_ => prepareStatements())
      .setEventHandler[UserCreated](e => insertUser(e.event))
      .setEventHandler[TenantCreated](e => insertTenant(e.event))
      .build

  override def aggregateTags = UserEvent.Tag.allTags

  private def insertUser(user: UserCreated) = {
    Future.successful(immutable.Seq(insertUserForEmailStatement.bind(user.userId, user.username, user.email, user.firstName, user.lastName, user.hashedPassword)))
  }

  private def insertTenant(tenantEvent: TenantCreated) = {
    Future.successful(immutable.Seq(insertUserForTenantStatement.bind(
      tenantEvent.tenant.userId,
      tenantEvent.tenant.name,
      tenantEvent.tenant.address,
      tenantEvent.tenant.phone,
      tenantEvent.tenant.province,
      tenantEvent.tenant.city,
      tenantEvent.tenant.county,
      tenantEvent.tenant.description,
      tenantEvent.tenant.credentials,
      tenantEvent.tenant.status
    )))
  }

  private def createTables() = {
    for {
      _ <- session.executeCreateTable(
        """
          |CREATE TABLE IF NOT EXISTS users (
          |  userId text PRIMARY KEY,
          |  username text,
          |  email text,
          |  first_name text,
          |  last_name text,
          |  hashed_password text
          |  )
        """.stripMargin)

      _ <- session.executeCreateTable(
        """
          |CREATE TABLE IF NOT EXISTS tenant_by_user (
          |  userId text,
          |  name text,
          |  address text,
          |  phone text,
          |  province text,
          |  city text,
          |  county text,
          |  description text,
          |  credentials list<text>,
          |  status text,
          |  PRIMARY KEY (userId)
          |  )
        """.stripMargin)
    } yield Done
  }

  private def prepareStatements() = {
    for {
      insertUserForEmail <- session.prepare(
        """
          |INSERT INTO users(
          |  userId,
          |  username,
          |  email,
          |  first_name,
          |  last_name,
          |  hashed_password
          |) VALUES (?, ?, ?, ?, ?, ?)
        """.stripMargin)

      insertUserForTenant <- session.prepare(
        """
          |INSERT INTO tenant_by_user(
          |  userId,
          |  name,
          |  address,
          |  phone,
          |  province,
          |  city,
          |  county,
          |  description,
          |  credentials,
          |  status
          |) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """.stripMargin)
    } yield {
      insertUserForEmailStatement = insertUserForEmail
      insertUserForTenantStatement = insertUserForTenant
      Done
    }
  }
}