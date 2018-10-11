/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

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
