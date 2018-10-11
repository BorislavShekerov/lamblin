/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.core.model.annotation

/** Defines the annotation to be used for marking a controller. */
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FILE)
@Retention(AnnotationRetention.RUNTIME)
annotation class Controller
