package com.lamblin.core

/**
 * Defines the access point for the controllers registered with the framework.
 */
class ControllerRegistry(private val controllers: Set<Any>) {

    /** Finds the controller instance of a given class. */
    fun controllerForClass(controllerClass: Class<*>) = controllers.find { it.javaClass == controllerClass }

    /** Retrieves the [Class] for each controller. */
    fun controllerClasses() = controllers.map { it::class.java }

}
