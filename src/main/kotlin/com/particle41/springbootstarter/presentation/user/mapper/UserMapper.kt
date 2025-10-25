package com.particle41.springbootstarter.presentation.user.mapper

import com.particle41.springbootstarter.domain.user.model.User
import com.particle41.springbootstarter.presentation.user.dto.UserRequest
import com.particle41.springbootstarter.presentation.user.dto.UserResponse
import org.springframework.stereotype.Component

@Component
class UserMapper {

    fun toDomain(request: UserRequest): User =
        User.create(request.name, request.email)

    fun toResponse(user: User): UserResponse =
        UserResponse(
            id = user.id.toString(),
            name = user.name,
            email = user.email,
            createdAt = user.createdAt,
            updatedAt = user.updatedAt
        )
}
