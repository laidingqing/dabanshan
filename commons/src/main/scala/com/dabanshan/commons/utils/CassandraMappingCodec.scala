package com.dabanshan.commons.utils

import com.datastax.driver.core.{Session, TypeCodec}
import com.datastax.driver.extras.codecs.MappingCodec
import com.datastax.driver.extras.codecs.arrays.AbstractPrimitiveArrayCodec
/**
  * Created by skylai on 2017/10/11.
  */
class BigDecimalCodec extends MappingCodec(TypeCodec.decimal(), classOf[scala.math.BigDecimal]) {
  override def serialize(value: scala.math.BigDecimal): java.math.BigDecimal = value.bigDecimal
  override def deserialize(value: java.math.BigDecimal): scala.math.BigDecimal = BigDecimal(value)
}
