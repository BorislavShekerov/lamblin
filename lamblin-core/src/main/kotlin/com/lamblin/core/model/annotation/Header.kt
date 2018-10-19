/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.core.model.annotation

/**
 * Defines the mechanism for annotating endpoint method parameters capturing request headers.
 *
 * E.g @Header("Authorization") will retrieve the "Authorization" header from the request if present,
 * otherwise a null value will be injected if the param type is nullable
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Header(val value: String)