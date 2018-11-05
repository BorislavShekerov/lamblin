/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.core

import com.lamblin.core.exception.MissingControllerAnnotationException
import com.lamblin.core.model.annotation.Controller
import kotlin.reflect.KClass

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

    /** Finds the controller frontController of a given class. */
    fun controllerForClass(controllerClass: KClass<*>) = controllers.find { it::class == controllerClass }

    /** Retrieves the [Class] for each controller. */
    fun controllerClasses() = controllers.map { it::class }

}
