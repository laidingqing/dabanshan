package com.dabanshan.tenant.api.model.response

import play.api.libs.json.{Format, Json}

/**
  * Created by skylai on 2017/10/9.
  */
case class GetTenantDone(
  id: String,
  userId: String,
  name: String,
  address: Option[String],
  phone: Option[String],
  province: Option[String],
  city: Option[String],
  county: Option[String]
)
object GetTenantDone {
  implicit val format: Format[GetTenantDone] = Json.format
}