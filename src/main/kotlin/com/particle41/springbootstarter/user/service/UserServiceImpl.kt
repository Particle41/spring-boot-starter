package com.particle41.springbootstarter.user.service

import com.particle41.springbootstarter.user.dto.UserRequest
import com.particle41.springbootstarter.user.dto.UserResponse
import com.particle41.springbootstarter.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService {
    override fun createUser(request: UserRequest): UserResponse {
        TODO("Not yet implemented")
    }

    override fun getUserById(id: Long): UserResponse? {
        TODO("Not yet implemented")
    }

    override fun getAllUsers(): List<UserResponse> {
        TODO("Not yet implemented")
    }

    override fun updateUser(
        id: Long,
        request: UserRequest
    ): UserResponse? {
        TODO("Not yet implemented")
    }

    override fun deleteUser(id: Long) {
        TODO("Not yet implemented")
    }
}