package com.particle41.springbootstarter.modules.user.infrastructure.repository

import com.particle41.springbootstarter.modules.user.infrastructure.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface SpringDataUserRepository : JpaRepository<UserEntity, UUID> {
}
