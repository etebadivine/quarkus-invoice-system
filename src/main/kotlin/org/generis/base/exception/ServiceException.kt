package org.generis.base.exception


class ServiceException(override val message: String?, val code:Int, override val cause: Throwable?) : RuntimeException(message, cause) {
    constructor(code: Int, message: String?) : this(message, code, null)

    constructor(code: Int, message: String?,cause: Throwable?) : this(message, code, cause)

}
