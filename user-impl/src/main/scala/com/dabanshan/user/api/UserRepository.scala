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
class UserRepository (session: CassandraSession)(implicit ec: ExecutionContext) {

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
    Future.successful(immutable.Seq(insertUserForEmailStatement.bind(user.userId, user.email)))
  }

  private def createTables() = {
    for {
      _ <- session.executeCreateTable(
        """
          |CREATE TABLE IF NOT EXISTS userByEmail (
          |  userId text,
          |  email text PRIMARY KEY
          |  )
        """.stripMargin)
    } yield Done
  }

  private def prepareStatements() = {
    for {
      insertUserForEmail <- session.prepare(
        """
          |INSERT INTO userByEmail(
          |  userId,
          |  email
          |) VALUES (?, ?)
        """.stripMargin)
    } yield {
      insertUserForEmailStatement = insertUserForEmail
      Done
    }
  }
}