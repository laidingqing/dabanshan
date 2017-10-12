package com.dabanshan.products.impl.product

import akka.Done
import com.dabanshan.commons.utils.PaginatedSequence
import com.dabanshan.product.api.model.ProductStatus
import com.dabanshan.product.api.model.response.ProductSummary
import com.datastax.driver.core.{PreparedStatement, Row}
import com.lightbend.lagom.scaladsl.persistence.ReadSideProcessor
import com.lightbend.lagom.scaladsl.persistence.cassandra.{CassandraReadSide, CassandraSession}
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.immutable
import scala.concurrent.{ExecutionContext, Future}
import scala.language.implicitConversions
/**
  * Created by skylai on 2017/9/30.
  */
class ProductRepository (session: CassandraSession)(implicit ec: ExecutionContext) {

  /**
    * 分页按状态及供应商查询
    * @param creatorId 供应商编号
    * @param status 状态
    * @param page
    * @param pageSize
    * @return
    */
  def getProductsForUser(creatorId: String, status: ProductStatus.Status, page: Int, pageSize: Int): Future[PaginatedSequence[ProductSummary]] = {
    val offset = page * pageSize
    val limit = (page + 1) * pageSize
    for {
      count <- countProductsByCreatorInStatus(creatorId, status)
      items <- if (offset > count) Future.successful(Nil)
      else selectItemsByCreatorInStatus(creatorId, status, offset, limit)
    } yield {
      PaginatedSequence(items, page, pageSize, count)
    }
  }



  private def countProductsByCreatorInStatus(creatorId: String, status: ProductStatus.Status) = {
    session.selectOne("""
      SELECT COUNT(*) FROM productSummaryByCreator
      WHERE creatorId = ? AND status = ?
      ORDER BY status ASC, itemId DESC
                      """,
      creatorId, status.toString).map {
      case Some(row) => row.getLong("count").toInt
      case None => 0
    }
  }

  private def selectItemsByCreatorInStatus(creatorId: String, status: ProductStatus.Status, offset: Int, limit: Int) = {
    session.selectAll("""
      SELECT * FROM productSummaryByCreator
      WHERE creatorId = ? AND status = ?
      ORDER BY status ASC, itemId DESC
      LIMIT ?
                      """, creatorId, status.toString, Integer.valueOf(limit)).map { rows =>
      rows.drop(offset)
        .map(convertProductSummary)
    }
  }

  private def convertProductSummary(item: Row): ProductSummary = {
    ProductSummary(
      item.getString("productId"),
      item.getString("name"),
      item.getDecimal("price"),
      item.getString("unit"),
      item.getString("category"),
      item.getString("creatorId"),
      ProductStatus.withName(item.getString("status"))
    )
  }

}


class ProductEventProcessor(session: CassandraSession, readSide: CassandraReadSide)(implicit ec: ExecutionContext)
  extends ReadSideProcessor[ProductEvent]
{
  private var insertProductCreatorStatement: PreparedStatement = null
  private var insertProductSummaryStatement: PreparedStatement = null
  private var insertProductWithCategoryStatement: PreparedStatement = null
  private var updateProductThumbnailsStatement: PreparedStatement = null

  val log: Logger = LoggerFactory.getLogger(getClass)

  override def buildHandler =
    readSide.builder[ProductEvent]("productEventOffset")
      .setGlobalPrepare(createTables)
      .setPrepare(_ => prepareStatements())
      .setEventHandler[ProductCreated](e => insertProduct(e.event))
      .setEventHandler[ProductThumbnailsCreated](e => updateThumbnails(e.entityId, e.event))
      .build

  override def aggregateTags = ProductEvent.Tag.allTags

  private def insertProduct(productCreated: ProductCreated) = {
    Future.successful(immutable.Seq(
      insertProductCreator(productCreated.product),
      insertProductSummaryByCreator(productCreated.product)
    ))
  }

  private def insertProductCreator(product: Product) = {
    insertProductCreatorStatement.bind(product.id, product.creator)
  }

  private def updateThumbnails(productId:String, ptc: ProductThumbnailsCreated) = {
    selectProductCreator(productId).map{
      case None => throw new IllegalStateException("No Product found for productId " + productId)
      case Some(row) =>
        val creatorId = row.getString("creatorId")
        log.info("thumbnails ids: " + ptc.ids)
        List(updateProductThumbnailsStatement.bind(ptc.ids, creatorId, productId))

    }
  }

  private def insertProductSummaryByCreator(p: Product) = {
    insertProductSummaryStatement.bind(
      p.creator,
      p.id,
      p.name,
      p.price,
      p.unit,
      p.category,
      p.description.getOrElse(null),
      p.thumbnails.getOrElse(null),
      p.details.getOrElse(null),
      p.status.toString
    )
  }

  private def createTables() = {
    for {
      _ <- session.executeCreateTable(
        """
          |CREATE TABLE IF NOT EXISTS productCreator (
          | productId text PRIMARY KEY,
          | creatorId text
          |)
        """.stripMargin)

      _ <- session.executeCreateTable(
        """
          |CREATE TABLE IF NOT EXISTS productSummaryByCreator (
          |  creatorId text,
          |  productId text,
          |  name text,
          |  price decimal,
          |  unit text,
          |  category text,
          |  description text,
          |  thumbnails list<text>,
          |  details list<text>,
          |  status text,
          |  PRIMARY KEY (creatorId, productId)
          |  )WITH CLUSTERING ORDER BY (productId DESC)
        """.stripMargin)

      _ <- session.executeCreateTable(
        """
          |CREATE TABLE IF NOT EXISTS productsWithCategory (
          |  productId text PRIMARY KEY,
          |  category text
          |  )
        """.stripMargin)

    } yield Done
  }

  private def selectProductCreator(productId: String) = {
    session.selectOne("SELECT * FROM productCreator WHERE productId = ?", productId)
  }

  private def prepareStatements() = {
    for {
      insertProductCreator <- session.prepare("""
        INSERT INTO productCreator(productId, creatorId) VALUES (?, ?)
      """)
      insertProductSummary <- session.prepare(
        """
          |INSERT INTO productSummaryByCreator(
          |  creatorId,
          |  productId,
          |  name,
          |  price,
          |  unit,
          |  category,
          |  description,
          |  thumbnails,
          |  details,
          |  status
          |) VALUES (?,?,?,?,?,?,?,?,?,?)
        """.stripMargin)

      insertProductWithCategory <- session.prepare(
        """
          |INSERT INTO productsWithCategory(
          |  productId,
          |  category
          |) VALUES (?, ?)
        """.stripMargin)

      updateProductThumbnails <- session.prepare(
        """
        | UPDATE productSummaryByCreator SET thumbnails = thumbnails + ? WHERE creatorId = ? AND productId = ?
        """.stripMargin)

    } yield {
      insertProductCreatorStatement = insertProductCreator
      insertProductSummaryStatement = insertProductSummary
      insertProductWithCategoryStatement = insertProductWithCategory
      updateProductThumbnailsStatement = updateProductThumbnails
      Done
    }
  }
}