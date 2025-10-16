package com.particle41.springbootstarter.modules.user.domain.repository

import com.particle41.springbootstarter.modules.user.domain.model.User
import java.util.UUID

interface UserRepository {
    fun save(user: User): User
    fun findById(id: UUID): User?
    fun findAll(): List<User>
    fun delete(id: UUID)
}
