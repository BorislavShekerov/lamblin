/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.core.exception

/** Thrown when event controller class is missing [Controller] annotation.  */
class MissingControllerAnnotationException(message: String) : RuntimeException(message)
