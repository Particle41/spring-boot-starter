package com.particle41.springbootstarter.user.repository

import com.particle41.springbootstarter.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
}