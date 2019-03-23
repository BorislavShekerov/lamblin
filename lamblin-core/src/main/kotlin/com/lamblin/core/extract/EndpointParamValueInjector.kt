/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.core.extract

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.lamblin.core.model.HandlerMethod
import kotlin.reflect.KClass
import kotlin.reflect.KParameter

/** Defines the mechanism for injecting the values into the endpoint params, based on the request details. */
internal interface EndpointParamValueInjector {

    /**
     * Computes the [HandlerMethod] param values using the details of the [APIGatewayProxyRequestEvent].
     * Returns an ordered paramAnnotationMappedName => paramValue map.
     */
    fun injectParamValues(
        request: APIGatewayProxyRequestEvent,
        handlerMethod: HandlerMethod,
        paramAnnotationMappedNameToParam: Map<String, KParameter>
    ): Map<String, Any?>

    fun castParamToRequiredType(paramType: KClass<*>?, paramValue: Any) = when {
        paramValue is Array<*> -> handleMultiValueParam(paramType, paramValue)
        else -> handleSingleValueParam(paramType, paramValue)
    }

    fun handleSingleValueParam(paramType: KClass<*>?, paramValue: Any) = when(paramType) {
        // Primitive types
        Byte::class -> (paramValue as String).toByte()
        Short::class -> (paramValue as String).toShort()
        Int::class -> (paramValue as String).toInt()
        Long::class -> (paramValue as String).toLong()
        Double::class -> (paramValue as String).toDouble()
        Float::class -> (paramValue as String).toFloat()
        Char::class -> (paramValue as String)[0]
        Boolean::class -> (paramValue as String).toBoolean()

        // Array types
        Array<Byte>::class -> byteArrayOf((paramValue as String).toByte())
        Array<Short>::class -> shortArrayOf((paramValue as String).toShort())
        Array<Int>::class -> intArrayOf((paramValue as String).toInt())
        Array<Long>::class -> longArrayOf((paramValue as String).toLong())
        Array<Float>::class -> floatArrayOf((paramValue as String).toFloat())
        Array<Double>::class -> doubleArrayOf((paramValue as String).toDouble())
        Array<Char>::class -> charArrayOf((paramValue as String)[0])
        Array<Boolean>::class -> booleanArrayOf((paramValue as String).toBoolean())

        else -> paramValue
    }

    fun handleMultiValueParam(paramType: KClass<*>?, paramValue: Array<*>) = when(paramType) {
        // Primitive types
        Byte::class -> (paramValue.first() as String).toByte()
        Short::class -> (paramValue.first() as String).toShort()
        Int::class -> (paramValue.first() as String).toInt()
        Long::class -> (paramValue.first() as String).toLong()
        Double::class -> (paramValue.first() as String).toDouble()
        Float::class -> (paramValue.first() as String).toFloat()
        Char::class -> (paramValue.first() as String)[0]
        Boolean::class -> (paramValue.first() as String).toBoolean()
        String::class -> paramValue.first() as String

        // Array types
        Array<Byte>::class -> paramValue.map { (it as String).toByte() }.toByteArray()
        Array<Short>::class -> paramValue.map { (it as String).toShort() }.toShortArray()
        Array<Int>::class -> paramValue.map { (it as String).toInt() }.toIntArray()
        Array<Long>::class -> paramValue.map { (it as String).toLong() }.toLongArray()
        Array<Float>::class -> paramValue.map { (it as String).toFloat() }.toFloatArray()
        Array<Double>::class -> paramValue.map { (it as String).toDouble() }.toDoubleArray()
        Array<Char>::class -> paramValue.map { (it as String)[0] }.toCharArray()
        Array<Boolean>::class -> paramValue.map { (it as String).toBoolean() }.toBooleanArray()

        else -> paramValue
    }
}

