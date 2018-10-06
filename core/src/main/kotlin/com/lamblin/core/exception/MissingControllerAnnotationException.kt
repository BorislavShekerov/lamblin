package com.lamblin.core.exception

/** Thrown when event controller class is missing [Controller] annotation.  */
class MissingControllerAnnotationException(message: String) : RuntimeException(message)
