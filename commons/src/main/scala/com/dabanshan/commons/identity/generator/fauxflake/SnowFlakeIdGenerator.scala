package com.dabanshan.commons.identity.generator.fauxflake

import com.dabanshan.commons.identity.generator.IdGeneratorProvider
import com.github.rholder.fauxflake.DefaultIdGenerator
import com.github.rholder.fauxflake.provider.SystemTimeProvider
import com.github.rholder.fauxflake.provider.twitter.SnowflakeEncodingProvider

import scala.concurrent.duration.Duration

/**
  * Created by skylai on 2017/8/31.
  */
class SnowFlakeIdGenerator extends IdGeneratorProvider {

  private val machineIdProvider = new RequestFormMachineIdProvider

  private val systemTimeProvider = new SystemTimeProvider
  private val snowFlakeEncodingProvider = new SnowflakeEncodingProvider(machineIdProvider.getMachineId)
  private val snowFlakeIdGenerator = new DefaultIdGenerator(systemTimeProvider, snowFlakeEncodingProvider)

  override def nextId: Long = snowFlakeIdGenerator.generateId(Duration("1 second").toSeconds.toInt).asLong
}