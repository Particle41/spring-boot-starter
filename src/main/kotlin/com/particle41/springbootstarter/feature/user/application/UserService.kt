package com.particle41.springbootstarter.feature.user.application

import com.particle41.springbootstarter.feature.user.domain.User

interface UserService {
    fun fetchOne(id: Long): User
    fun fetchAll(): List<User>
    fun create(user: User): User
    fun update(id: Long, user: User): User
    fun delete(id: Long)
}