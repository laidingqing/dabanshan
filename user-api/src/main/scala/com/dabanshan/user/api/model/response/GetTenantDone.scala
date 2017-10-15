package com.dabanshan.user.api.model.response

import com.dabanshan.user.api.model.TenantStatus
import play.api.libs.json.{Format, Json}

/**
  * Created by skylai on 2017/10/9.
  */
case class GetTenantDone(
  userId: String,
  name: String,
  address: Option[String],
  phone: Option[String],
  province: Option[String],
  city: Option[String],
  county: Option[String],
  description: Option[String],
  credentials: Option[List[String]],
  status: TenantStatus.Status
)
object GetTenantDone {
  implicit val format: Format[GetTenantDone] = Json.format
}