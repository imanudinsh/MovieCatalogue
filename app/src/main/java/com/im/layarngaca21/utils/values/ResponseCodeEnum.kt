package com.im.layarngaca21.utils.values

enum class ResponseCodeEnum(val code: Int) {
    OK(200),
    MODIFIED(304),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500)
}