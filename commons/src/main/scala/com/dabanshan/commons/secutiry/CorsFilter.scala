package com.dabanshan.commons.secutiry

import com.lightbend.lagom.scaladsl.api.transport.{HeaderFilter, RequestHeader, ResponseHeader}

/**
  * Created by skylai on 2017/10/16.
  */
class CorsFilter extends HeaderFilter{
  override def transformClientRequest(request: RequestHeader): RequestHeader = request

  override def transformServerRequest(request: RequestHeader): RequestHeader = request

  override def transformServerResponse(response: ResponseHeader, request: RequestHeader): ResponseHeader = {
    response.
      withHeader("Access-Control-Allow-Origin", "*")
      .withHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization");
  }

  override def transformClientResponse(response: ResponseHeader, request: RequestHeader): ResponseHeader = response
}
