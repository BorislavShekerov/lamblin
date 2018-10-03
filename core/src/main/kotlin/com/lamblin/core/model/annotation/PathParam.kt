package com.lamblin.core.model.annotation

/**
 * Defines the mechanism for annotating endpoint method parameters capturing request path parameters.
 *
 * E.g /test/{param}
 * The annotation to use here in order to target "{param}" will be @PathParam("param") String param
 */
@Target(
        AnnotationTarget.VALUE_PARAMETER,
        AnnotationTarget.FUNCTION,
        AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.PROPERTY_SETTER,
        AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class PathParam(val value: String)
