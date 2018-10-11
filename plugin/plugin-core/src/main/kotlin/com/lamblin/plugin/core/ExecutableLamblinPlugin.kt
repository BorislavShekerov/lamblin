package com.lamblin.plugin.core

import com.lamblin.plugin.core.model.PluginExecutionResult

/** Defines a mechanism for plugging in additional Lamblin features. */
interface ExecutableLamblinPlugin<T> {

    /** Executes the plugin specific functionality. */
    fun execute(metadata: T? = null): PluginExecutionResult
}
