package com.lamblin.core.model.annotation

/**
 * Defines the mechanism for annotating endpoint method query params.
 *
 * E.g /test?paramA=foo
 * The annotation to use here in order to target "paramA" will be @QueryParam("paramA") String param
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class QueryParam(
        /** The query param name. */
        val value: String,

        /** Defines if the presence of the query param is required */
        val required: Boolean = true,

        /** The default value to use in absence of the param in the request.  */
        val defaultValue: String = "")
