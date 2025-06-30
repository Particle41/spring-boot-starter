package com.particle41.springbootstarter.user.service

import com.particle41.springbootstarter.user.dto.UserRequest
import com.particle41.springbootstarter.user.dto.UserResponse

interface UserService {

    fun createUser(request: UserRequest): UserResponse

    fun getUserById(id: Long): UserResponse?

    fun getAllUsers(): List<UserResponse>

    fun updateUser(id: Long, request: UserRequest): UserResponse?

    fun deleteUser(id: Long)
}