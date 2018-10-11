/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.core.model.annotation

/**
 * Defines the mechanism for annotating endpoint method parameters capturing request path parameters.
 *
 * E.g /test/{param}
 * The annotation to use here in order to target "{param}" will be @PathParam("param") String param
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class PathParam(val value: String)
