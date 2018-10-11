package com.lamblin.plugin.warmup

import com.lamblin.plugin.core.ExecutableLamblinPlugin
import com.lamblin.plugin.core.model.PluginExecutionResult
import org.slf4j.LoggerFactory

const val DEFAULT_WARMUP_EVENT_KEY = "warmup_event"
private val LOGGER = LoggerFactory.getLogger(WarmupLambdaPlugin::class.java)

/** Defines the plugin handling warmup events. */
class WarmupLambdaPlugin(
    private val warmupEventKey: String = DEFAULT_WARMUP_EVENT_KEY
) : ExecutableLamblinPlugin<Map<String, Any>> {

    override fun execute(event: Map<String, Any>?) =

        if (event?.containsKey(warmupEventKey) == true) {
            LOGGER.info("Triggered by a warm-up event.")

            PluginExecutionResult.SUCCESS
        } else {
            PluginExecutionResult.SKIPPED
        }
}
