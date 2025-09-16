package com.particle41.springbootstarter.feature.user.domain

interface UserRepository {
    fun save(user: User): User
    fun findById(id: Long): User?
    fun findAll(): List<User>
    fun delete(id: Long)
}