package com.particle41.springbootstarter.modules.user.infrastructure.repository

import com.particle41.springbootstarter.modules.user.domain.model.User
import com.particle41.springbootstarter.modules.user.domain.repository.UserRepository
import com.particle41.springbootstarter.modules.user.infrastructure.mapper.UserMapper
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
    private val springDataRepo: SpringDataUserRepository
) : UserRepository {

    override fun save(user: User): User {
        val entity = UserMapper.toEntity(user)
        val savedEntity = springDataRepo.save(entity)
        return UserMapper.toDomain(savedEntity)
    }

    override fun findById(id: Long): User? =
        springDataRepo.findById(id).map(UserMapper::toDomain).orElse(null)

    override fun findAll(): List<User> =
        springDataRepo.findAll().map(UserMapper::toDomain)

    override fun delete(id: Long) =
        springDataRepo.deleteById(id)
}