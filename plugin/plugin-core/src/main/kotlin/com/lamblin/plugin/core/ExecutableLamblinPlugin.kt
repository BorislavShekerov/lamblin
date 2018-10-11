package com.lamblin.plugin.core

import com.lamblin.plugin.core.model.PluginType
import com.lamblin.plugin.core.model.PluginExecutionResult

/** Defines a mechanism for plugging in additional Lamblin features. */
interface ExecutableLamblinPlugin {

    /** Gets the [PluginType] of the plugin implementation. */
    fun getPluginType(): PluginType

    /** Executes the plugin specific functionality. */
    fun execute(pluginInput: Any? = null): PluginExecutionResult
}
