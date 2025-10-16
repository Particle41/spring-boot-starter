package com.particle41.springbootstarter.modules.user.presentation.mapper

import com.particle41.springbootstarter.modules.user.domain.model.User
import com.particle41.springbootstarter.modules.user.presentation.dto.UserRequest
import com.particle41.springbootstarter.modules.user.presentation.dto.UserResponse
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.UUID

@Component
class UserMapper {

    fun toDomain(request: UserRequest): User {
        val now = LocalDateTime.now()
        return User(
            id = UUID.randomUUID(),
            name = request.name,
            email = request.email,
            createdAt = now,
            updatedAt = now
        )
    }

    fun toResponse(user: User): UserResponse =
        UserResponse(
            id = user.id.toString(),
            name = user.name,
            email = user.email,
            createdAt = user.createdAt,
            updatedAt = user.updatedAt
        )
}
