package com.particle41.springbootstarter.domain.user.repository

import com.particle41.springbootstarter.domain.user.model.User
import java.util.UUID

interface UserRepository {
    fun save(user: User): User
    fun findById(id: UUID): User?
    fun findAll(): List<User>
    fun delete(id: UUID)
}
