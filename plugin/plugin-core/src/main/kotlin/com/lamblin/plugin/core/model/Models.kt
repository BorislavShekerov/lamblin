package com.lamblin.plugin.core.model

import com.lamblin.plugin.core.ExecutableLamblinPlugin

/** Defines the set of plugins available. */
enum class PluginType {
    WARMUP
}

/** Defines the result of executing a [ExecutableLamblinPlugin] */
enum class PluginExecutionResult {

    /**
     * Plugin execution can be skipped if a precondition
     * for executing the plugin is not satisfied.
     */
    SKIPPED,

    /** Defines that the plugin executed successfully. */
    SUCCESS,

    /** Defines that a problem occurred during plugin execution. */
    FAILURE
}
