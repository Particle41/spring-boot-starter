package com.particle41.springbootstarter.feature.user.infrastructure.mapper

import com.particle41.springbootstarter.feature.user.domain.User
import com.particle41.springbootstarter.feature.user.infrastructure.persistence.UserEntity

object UserMapper {
    fun toDomain(entity: UserEntity): User =
        User(
            id = entity.id,
            name = entity.name,
            email = entity.email
        )

    fun toEntity(domain: User): UserEntity =
        UserEntity(
            id = domain.id ?: 0,
            name = domain.name,
            email = domain.email
        )
}