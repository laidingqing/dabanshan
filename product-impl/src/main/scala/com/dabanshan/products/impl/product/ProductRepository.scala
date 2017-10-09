package com.dabanshan.products.impl.product

import akka.Done
import com.datastax.driver.core.PreparedStatement
import com.lightbend.lagom.scaladsl.persistence.ReadSideProcessor
import com.lightbend.lagom.scaladsl.persistence.cassandra.{CassandraReadSide, CassandraSession}

import scala.collection.immutable
import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by skylai on 2017/9/30.
  */
class ProductRepository (session: CassandraSession)(implicit ec: ExecutionContext) {


}


class ProductEventProcessor(session: CassandraSession, readSide: CassandraReadSide)(implicit ec: ExecutionContext)
  extends ReadSideProcessor[ProductEvent]
{
  private var insertProductStatement: PreparedStatement = null
  private var insertProductWithCategoryStatement: PreparedStatement = null

  override def buildHandler =
    readSide.builder[ProductEvent]("productEventOffset")
      .setGlobalPrepare(createTables)
      .setPrepare(_ => prepareStatements())
      .setEventHandler[ProductCreated](e => insertProduct(e.event))
      .build

  override def aggregateTags =
    ProductEvent.Tag.allTags

  private def insertProduct(user: ProductCreated) = {
    Future.successful(immutable.Seq(insertProductStatement.bind()))
  }

  private def createTables() = {
    for {
      _ <- session.executeCreateTable(
        """
          |CREATE TABLE IF NOT EXISTS products (
          |  productId text PRIMARY KEY,
          |  )
        """.stripMargin)

      _ <- session.executeCreateTable(
        """
          |CREATE TABLE IF NOT EXISTS productsWithCategory (
          |  productId text PRIMARY KEY,
          |  categoryId text
          |  )
        """.stripMargin)

    } yield Done
  }

  private def prepareStatements() = {
    for {
      insertProduct <- session.prepare(
        """
          |INSERT INTO products(
          |  productId
          |) VALUES (?)
        """.stripMargin)

      insertProductWithCategory <- session.prepare(
        """
          |INSERT INTO productsWithCategory(
          |  productId,
          |  categoryId
          |) VALUES (?, ?)
        """.stripMargin
      )

    } yield {
      insertProductStatement = insertProduct
      insertProductWithCategoryStatement = insertProductWithCategory
      Done
    }
  }
}