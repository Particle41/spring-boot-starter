package com.particle41.springbootstarter.modules.user.infrastructure.mapper

import com.particle41.springbootstarter.modules.user.domain.model.User
import com.particle41.springbootstarter.modules.user.infrastructure.entity.UserEntity

object UserMapper {
    fun toDomain(entity: UserEntity): User =
        User(
            id = entity.id,
            name = entity.name,
            email = entity.email,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )

    fun toEntity(domain: User): UserEntity =
        UserEntity(
            id = domain.id,
            name = domain.name,
            email = domain.email,
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt
        )
}
