package com.particle41.springbootstarter.common.exceptions

/**
 * Base exception for all domain-related exceptions.
 * Represents business rule violations and domain-specific errors.
 */
abstract class DomainException(
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)