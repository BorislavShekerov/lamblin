package com.lamblin.core.model

/** Defines the status codes supported. */
enum class StatusCode(val code: Int) {
    OK(200),
    CREATED(201),
    ACCEPTED(202),
    NON_AUTHORITATIVE_INFORMATION(203),
    NO_CONTENT(204),
    RESET_CONTENT(205),
    PARTIAL_CONTENT(206),

    MULTIPLE_CHOICES(300),
    MOVED_PERMANENTLY(301),
    FOUND(302),
    SEE_OTHER(303),
    NOT_MODIFIED(304),
    USE_PROXY(305),
    SWITCH_PROXY(306),
    TEMPORARY_REDIRECT(307),
    PERMANENT_REDIRECT(308),

    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    PAYMENT_REQUIRED(402),
    FORBIDDEN(403),
    NOT_FOUND(404),
    METHOD_NOT_ALLOWED(405),
    METHOD_NOT_ACCEPTED(406),
    PROXY_AUTHENTICATION_REQUIRED(407),
    REQUEST_TIMEOUT(408),
    CONFLICT(409),
    GONE(410),

    API_ERROR(500),
    NOT_IMPLEMENTED(501),
    BAD_GATEWAY(502),
    SERVICE_UNAVAILABLE(503)
}
