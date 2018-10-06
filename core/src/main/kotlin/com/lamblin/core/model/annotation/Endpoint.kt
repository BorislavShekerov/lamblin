package com.lamblin.core.model.annotation

import com.lamblin.core.model.HttpMethod

/**
 * Defines the mechanism for annotating controller endpoint methods.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Endpoint(val path: String, val method: HttpMethod)
