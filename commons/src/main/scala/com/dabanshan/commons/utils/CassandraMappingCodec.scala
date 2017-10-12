package com.dabanshan.commons.utils

import java.nio.ByteBuffer

import com.datastax.driver.core.{ProtocolVersion, TypeCodec}
import com.datastax.driver.extras.codecs.MappingCodec
import scala.collection.JavaConversions._
/**
  * Created by skylai on 2017/10/11.
  */
class BigDecimalCodec extends MappingCodec(TypeCodec.decimal(), classOf[scala.math.BigDecimal]) {
  override def serialize(value: scala.math.BigDecimal): java.math.BigDecimal = value.bigDecimal
  override def deserialize(value: java.math.BigDecimal): scala.math.BigDecimal = BigDecimal(value)
}

class StringListCodec extends MappingCodec(TypeCodec.list(TypeCodec.varchar()), classOf[scala.List[String]]) {
  override def serialize(value: scala.List[String]): java.util.List[String] = {
    val jl : java.util.List[String] = value
    jl
  }
  override def deserialize(value: java.util.List[String]): scala.List[String] = {
    val sl2 : List[String] = value.toList
    sl2
  }
}