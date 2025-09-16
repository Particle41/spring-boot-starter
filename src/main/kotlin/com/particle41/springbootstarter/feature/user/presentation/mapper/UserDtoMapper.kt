package com.particle41.springbootstarter.feature.user.presentation.mapper

import com.particle41.springbootstarter.feature.user.domain.User
import com.particle41.springbootstarter.feature.user.presentation.dto.UserRequest
import com.particle41.springbootstarter.feature.user.presentation.dto.UserResponse
import org.springframework.stereotype.Component

@Component
class UserDtoMapper {

    fun toDomain(request: UserRequest): User =
        User(
            id = null,
            name = request.name,
            email = request.email
        )

    fun toResponse(user: User): UserResponse =
        UserResponse(
            id = user.id!!,
            name = user.name,
            email = user.email
        )
}
