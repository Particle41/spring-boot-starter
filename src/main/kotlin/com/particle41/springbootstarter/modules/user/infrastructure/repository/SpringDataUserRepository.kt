package com.particle41.springbootstarter.modules.user.infrastructure.repository

import com.particle41.springbootstarter.modules.user.infrastructure.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SpringDataUserRepository : JpaRepository<UserEntity, Long> {
}