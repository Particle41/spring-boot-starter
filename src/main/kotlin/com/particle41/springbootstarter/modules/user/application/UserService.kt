package com.particle41.springbootstarter.modules.user.application

import com.particle41.springbootstarter.modules.user.domain.model.User

interface UserService {
    fun fetchOne(id: Long): User
    fun fetchAll(): List<User>
    fun create(user: User): User
    fun update(id: Long, user: User): User
    fun delete(id: Long)
}