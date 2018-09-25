package com.lamblin.core

import kotlin.reflect.KClass

/**
 * Defines the access point for the controllers registered with the framework.
 */
class ControllerRegistry(private val controllers: Set<Any>) {

    fun controllerForClass(controllerClass: KClass<*>) = controllers.find { it.javaClass == controllerClass.java }

    fun controllerClasses() = controllers.map { it::class }

}
