package com.dabanshan.commons.identity

import com.dabanshan.commons.identity.generator.IdGeneratorProvider
import com.dabanshan.commons.identity.generator.fauxflake.SnowFlakeIdGenerator

/**
  * Created by skylai on 2017/8/31.
  */

class Id(idGeneratorProvider: IdGeneratorProvider) {
  def randomID: Long = idGeneratorProvider.nextId
}

object Id {
  private lazy val defaultProvider = new SnowFlakeIdGenerator

  def apply(provider: String = ""): Id = provider.toLowerCase match {
    case "snowflake" => new Id(new SnowFlakeIdGenerator) // Twitter SnowFlake's Algorithm
    case _           => new Id(defaultProvider)
  }
}