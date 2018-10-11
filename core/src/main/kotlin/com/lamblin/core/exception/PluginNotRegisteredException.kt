package com.lamblin.core.exception

/** Thrown when execution of non-registered plugin is attempted.  */
class PluginNotRegisteredException(message: String) : RuntimeException(message)
