package com.particle41.springbootstarter.modules.user.domain.model

import java.time.LocalDateTime
import java.util.*

data class User(
    val id: UUID,
    val name: String,
    val email: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    fun update(name: String, email: String): User =
        copy(name = name, email = email, updatedAt = LocalDateTime.now())

    companion object {
        fun create(name: String, email: String): User {
            val now = LocalDateTime.now()
            return User(
                id = UUID.randomUUID(),
                name = name,
                email = email,
                createdAt = now,
                updatedAt = now
            )
        }
    }
}
