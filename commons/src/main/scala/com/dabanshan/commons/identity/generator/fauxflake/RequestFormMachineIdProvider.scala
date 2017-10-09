package com.dabanshan.commons.identity.generator.fauxflake

import com.github.rholder.fauxflake.api.MachineIdProvider

/**
  * Created by skylai on 2017/8/31.
  */
class RequestFormMachineIdProvider extends MachineIdProvider {
  override def getMachineId: Long = 1234L
}