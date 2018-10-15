/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.plugin.warmup

import com.lamblin.plugin.core.ExecutableLamblinPlugin
import com.lamblin.plugin.core.model.PluginExecutionResult
import com.lamblin.plugin.core.model.PluginType
import org.slf4j.LoggerFactory

const val DEFAULT_WARMUP_EVENT_KEY = "warmup_event"
private val LOGGER = LoggerFactory.getLogger(WarmupLambdaPlugin::class.java)

/** Defines the plugin handling warmup events. */
class WarmupLambdaPlugin(
    private val warmupEventKey: String = DEFAULT_WARMUP_EVENT_KEY) : ExecutableLamblinPlugin {

    override fun getPluginType() = PluginType.WARMUP

    override fun execute(pluginInput: Any?) =

        if ((pluginInput as? Map<String, Any>)?.containsKey(warmupEventKey) == true) {
            LOGGER.info("Triggered by a warm-up pluginInput.")

            PluginExecutionResult.SUCCESS
        } else {
            PluginExecutionResult.SKIPPED
        }
}
