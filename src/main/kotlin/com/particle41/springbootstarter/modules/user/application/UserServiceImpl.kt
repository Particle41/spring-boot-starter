package com.particle41.springbootstarter.modules.user.application

import com.particle41.springbootstarter.modules.user.domain.model.User
import com.particle41.springbootstarter.modules.user.domain.repository.UserRepository
import com.particle41.springbootstarter.modules.user.domain.exception.UserNotFoundException
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService {

    override fun fetchOne(id: Long): User =
        userRepository.findById(id) ?: throw UserNotFoundException(id)

    override fun fetchAll(): List<User> =
        userRepository.findAll()

    @Transactional
    override fun create(user: User): User =
        userRepository.save(user)

    @Transactional
    override fun update(id: Long, user: User): User {
        val existingUser = userRepository.findById(id) ?: throw UserNotFoundException(id)
        val updatedUser = existingUser.copy(
            name = user.name,
            email = user.email
        )
        return userRepository.save(updatedUser)
    }

    @Transactional
    override fun delete(id: Long) {
        val existingUser = userRepository.findById(id) ?: throw UserNotFoundException(id)
        userRepository.delete(existingUser.id!!)
    }
}
