package com.particle41.springbootstarter.modules.user.domain.repository

import com.particle41.springbootstarter.modules.user.domain.model.User

interface UserRepository {
    fun save(user: User): User
    fun findById(id: Long): User?
    fun findAll(): List<User>
    fun delete(id: Long)
}