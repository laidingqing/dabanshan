package com.dabanshan.products.api

import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraSession

import scala.concurrent.ExecutionContext

/**
  * Created by skylai on 2017/9/30.
  */
class ProductRepository (session: CassandraSession)(implicit ec: ExecutionContext) {

}
