package com.particle41.springbootstarter.infrastructure.user.repository

import com.particle41.springbootstarter.infrastructure.user.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface SpringDataUserRepository : JpaRepository<UserEntity, UUID> {
}
