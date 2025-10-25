package com.particle41.springbootstarter.shared.exceptions

/**
 * Base exception for domain entity not found scenarios.
 */
abstract class DomainNotFoundException(
    entityName: String,
    identifier: String,
    cause: Throwable? = null
) : DomainException("$entityName with id=$identifier not found", cause)
