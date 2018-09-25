package com.lamblin.core.model.annotation

/**
 * Defines the mechanism for marking a value object as the deserialization target of a POST request.
 *
 * The framework will attempt to deserialize the request body into the target object.
 */
@Target(
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.FUNCTION,
        AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.PROPERTY_SETTER,
        AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class RequestBody
