package com.particle41.springbootstarter.application.user

import com.particle41.springbootstarter.domain.pagination.Page
import com.particle41.springbootstarter.domain.pagination.Pageable
import com.particle41.springbootstarter.domain.user.model.User
import java.util.UUID

interface UserService {
    fun fetchOne(id: UUID): User
    fun fetchAll(): List<User>
    fun fetchAll(pageable: Pageable): Page<User>
    fun create(user: User): User
    fun update(id: UUID, user: User): User
    fun delete(id: UUID)
}
