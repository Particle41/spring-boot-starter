package com.particle41.springbootstarter.application.user

import com.particle41.springbootstarter.domain.user.model.User
import com.particle41.springbootstarter.domain.user.repository.UserRepository
import com.particle41.springbootstarter.domain.user.exception.UserNotFoundException
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService {

    override fun fetchOne(id: UUID): User =
        userRepository.findById(id) ?: throw UserNotFoundException(id.toString())

    override fun fetchAll(): List<User> =
        userRepository.findAll()

    @Transactional
    override fun create(user: User): User =
        userRepository.save(user)

    @Transactional
    override fun update(id: UUID, user: User): User {
        val existingUser = userRepository.findById(id) ?: throw UserNotFoundException(id.toString())
        val updatedUser = existingUser.update(user.name, user.email)
        return userRepository.save(updatedUser)
    }

    @Transactional
    override fun delete(id: UUID) {
        val existingUser = userRepository.findById(id) ?: throw UserNotFoundException(id.toString())
        userRepository.delete(existingUser.id)
    }
}
