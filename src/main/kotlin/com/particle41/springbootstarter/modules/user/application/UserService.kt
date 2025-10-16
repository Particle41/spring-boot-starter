package com.particle41.springbootstarter.modules.user.application

import com.particle41.springbootstarter.modules.user.domain.model.User
import java.util.UUID

interface UserService {
    fun fetchOne(id: UUID): User
    fun fetchAll(): List<User>
    fun create(user: User): User
    fun update(id: UUID, user: User): User
    fun delete(id: UUID)
}
