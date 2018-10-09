package com.lamblin.core

import com.lamblin.core.exception.MissingControllerAnnotationException
import com.lamblin.core.model.annotation.Controller

/**
 * Defines the access point for the controllers registered with the framework.
 */
class ControllerRegistry(private val controllers: Set<Any>) {

    init {
        controllers
            .asSequence()
            .find { !it::class.java.isAnnotationPresent(Controller::class.java) }
            ?.let {
                throw MissingControllerAnnotationException(
                    "${it::class.java.name} class not annotated with @Controller")
            }
    }

    /** Finds the controller instance of a given class. */
    fun controllerForClass(controllerClass: Class<*>) = controllers.find { it.javaClass == controllerClass }

    /** Retrieves the [Class] for each controller. */
    fun controllerClasses() = controllers.map { it::class.java }

}
