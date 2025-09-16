package com.particle41.springbootstarter.feature.user.infrastructure.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SpringDataUserRepository : JpaRepository<UserEntity, Long> {
}