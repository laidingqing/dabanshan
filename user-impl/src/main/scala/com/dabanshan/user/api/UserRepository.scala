package com.dabanshan.user.api

import akka.Done

import scala.concurrent.ExecutionContext
import com.datastax.driver.core.PreparedStatement
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
    val result = session.selectOne("SELECT id, username, hashed_password FROM users_by_username WHERE username = ?", username).map {
      case Some(row) => Option(
        UserByUsername(
          username = row.getString("username"),
          id = row.getString("id"),
          hashedPassword = row.getString("hashed_password")
        )
      )
      case None => Option.empty
    }
    result
  }



}

class UserEventProcessor(session: CassandraSession, readSide: CassandraReadSide)(implicit ec: ExecutionContext)
  extends ReadSideProcessor[UserEvent]
{
  private var insertUserForEmailStatement: PreparedStatement = null

  override def buildHandler =
    readSide.builder[UserEvent]("userEventOffset")
      .setGlobalPrepare(createTables)
      .setPrepare(_ => prepareStatements())
      .setEventHandler[UserCreated](e => insertUser(e.event))
      .build

  override def aggregateTags =
    UserEvent.Tag.allTags

  private def insertUser(user: UserCreated) = {
    Future.successful(immutable.Seq(insertUserForEmailStatement.bind(user.userId, user.email, user.password)))
  }

  private def createTables() = {
    for {
      _ <- session.executeCreateTable(
        """
          |CREATE TABLE IF NOT EXISTS users (
          |  userId text PRIMARY KEY,
          |  email text,
          |  password text
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
          |  email,
          |  password
          |) VALUES (?, ?, ?)
        """.stripMargin)
    } yield {
      insertUserForEmailStatement = insertUserForEmail
      Done
    }
  }
}