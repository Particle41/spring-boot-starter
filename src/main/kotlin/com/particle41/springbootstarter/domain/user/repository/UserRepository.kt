package com.particle41.springbootstarter.domain.user.repository

import com.particle41.springbootstarter.domain.pagination.Page
import com.particle41.springbootstarter.domain.pagination.Pageable
import com.particle41.springbootstarter.domain.user.model.User
import java.util.UUID

interface UserRepository {
    fun save(user: User): User
    fun findById(id: UUID): User?
    fun findAll(): List<User>
    fun findAll(pageable: Pageable): Page<User>
    fun delete(id: UUID)
}
