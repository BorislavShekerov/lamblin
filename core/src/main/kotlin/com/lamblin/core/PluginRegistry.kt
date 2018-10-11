package com.lamblin.core

import com.lamblin.core.exception.PluginNotRegisteredException
import com.lamblin.plugin.core.ExecutableLamblinPlugin
import com.lamblin.plugin.core.model.PluginExecutionResult
import com.lamblin.plugin.core.model.PluginType

/** Defines the plugin registry mechanism, serves as a gateway to plugin execution. */
internal interface PluginRegistry {

    /** Makes a plugin available for execution. */
    fun registerPlugin(plugin: ExecutableLamblinPlugin)

    /** Checks if a plugin has been registered. */
    fun isPluginRegistered(pluginType: PluginType): Boolean

    /** Executes a plugin of a given type. */
    fun executePlugin(plugin: PluginType, eventInput: Any): PluginExecutionResult

}

object DefaultPluginRegistry : PluginRegistry {

    private val pluginTypeToPlugin: MutableMap<PluginType, ExecutableLamblinPlugin> = mutableMapOf()

    override fun registerPlugin(plugin: ExecutableLamblinPlugin) {
        pluginTypeToPlugin[plugin.getPluginType()] = plugin
    }

    override fun isPluginRegistered(pluginType: PluginType) = pluginTypeToPlugin.containsKey(pluginType)

    override fun executePlugin(plugin: PluginType, pluginInput: Any) =
        pluginTypeToPlugin[plugin]
            ?.execute(pluginInput)
            ?: throw PluginNotRegisteredException("$plugin not registered.")

}
