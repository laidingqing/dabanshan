package com.dabanshan.products.impl.category

import akka.Done
import com.datastax.driver.core.PreparedStatement
import com.lightbend.lagom.scaladsl.persistence.ReadSideProcessor
import com.lightbend.lagom.scaladsl.persistence.cassandra.{CassandraReadSide, CassandraSession}

import scala.collection.immutable
import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by skylai on 2017/9/30.
  */
class CategoryRepository (session: CassandraSession)(implicit ec: ExecutionContext) {

}

class CategoryEventProcessor(session: CassandraSession, readSide: CassandraReadSide)(implicit ec: ExecutionContext)
  extends ReadSideProcessor[CategoryEvent]
{
  private var insertStatement: PreparedStatement = null
  private var insertCategoryProductStatement: PreparedStatement = null

  override def buildHandler =
    readSide.builder[CategoryEvent]("categoryEventOffset")
      .setGlobalPrepare(createTables)
      .setPrepare(_ => prepareStatements())
      .setEventHandler[CategoryCreated](e => insertCategory(e.event))
      .setEventHandler[CategoryWithProductCreated](e => insertProductWithCategory(e.event))
      .build

  override def aggregateTags = CategoryEvent.Tag.allTags

  private def insertCategory(category: CategoryCreated) = {
    Future.successful(immutable.Seq(insertStatement.bind(category.id, category.name)))
  }

  private def insertProductWithCategory(categoryWithProduct: CategoryWithProductCreated) = {
    Future.successful(immutable.Seq(insertCategoryProductStatement.bind(categoryWithProduct.id, categoryWithProduct.productId)))
  }

  private def createTables() = {
    for {
      _ <- session.executeCreateTable(
        """
          |CREATE TABLE IF NOT EXISTS categories (
          |  categoryId varchar PRIMARY KEY,
          |  name varchar
          |  )
        """.stripMargin)

      _ <- session.executeCreateTable("""
        CREATE TABLE IF NOT EXISTS categoriesWithProducts (
          categoryId varchar PRIMARY KEY,
          productId varchar
        )
        """.stripMargin)

    } yield Done
  }

  private def prepareStatements() = {
    for {
      insertCategory <- session.prepare(
        """
          |INSERT INTO categories(
          |  categoryId,
          |  name
          |) VALUES (?, ?)
        """.stripMargin)
      insertCategoryProduct <- session.prepare(
        """
          |INSERT INTO categoriesWithProducts(
          |  categoryId,
          |  productId
          |) VALUES (?, ?)
        """.stripMargin)
    } yield {
      insertStatement = insertCategory
      insertCategoryProductStatement = insertCategoryProduct
      Done
    }
  }
}