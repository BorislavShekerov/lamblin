package com.lamblin.core.security

import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class AccessControl(val roles: Array<String>, val authorizer: KClass<out RequestAuthorizer>)
