package com.dabanshan.product.api.model

/**
  * Created by skylai on 2017/9/1.
  */

case class Pricing(buy: BigDecimal, sell: BigDecimal, expenses: BigDecimal)
case class Product(id: String, name: String, description: Option[String], pricing: Pricing)


