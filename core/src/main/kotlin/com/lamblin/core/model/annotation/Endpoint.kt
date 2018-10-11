/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.core.model.annotation

import com.lamblin.core.model.HttpMethod

/** Defines the mechanism for annotating controller endpoint methods. */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Endpoint(val path: String, val method: HttpMethod)
