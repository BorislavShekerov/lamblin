/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.core.exception

/** Thrown when execution of non-registered plugin is attempted.  */
class PluginNotRegisteredException(message: String) : RuntimeException(message)
