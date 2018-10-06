package com.lamblin.core.model.annotation

/**
 * Defines the annotation to be used for marking a controller
 */
@Target(
        AnnotationTarget.CLASS,
        AnnotationTarget.FILE)
@Retention(AnnotationRetention.RUNTIME)
annotation class Controller
