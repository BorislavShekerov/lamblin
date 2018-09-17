package com.lamblin.core.model.annotation

import com.lamblin.core.model.HttpMethod

/**
 * Defines the mechanism for annotating controller endpoint methods.
 */
@Target(
        AnnotationTarget.CLASS,
        AnnotationTarget.FILE,
        AnnotationTarget.FUNCTION,
        AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Endpoint(val path: String, val method: HttpMethod)