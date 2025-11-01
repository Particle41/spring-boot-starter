package com.particle41.springbootstarter.infrastructure.user.repository

import com.particle41.springbootstarter.domain.pagination.Page
import com.particle41.springbootstarter.domain.pagination.Pageable
import com.particle41.springbootstarter.domain.user.model.User
import com.particle41.springbootstarter.domain.user.repository.UserRepository
import com.particle41.springbootstarter.infrastructure.pagination.mapper.PageableMapper
import com.particle41.springbootstarter.infrastructure.user.mapper.UserMapper
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class UserRepositoryImpl(
    private val springDataRepo: SpringDataUserRepository
) : UserRepository {

    override fun save(user: User): User {
        val entity = UserMapper.toEntity(user)
        val savedEntity = springDataRepo.save(entity)
        return UserMapper.toDomain(savedEntity)
    }

    override fun findById(id: UUID): User? =
        springDataRepo.findById(id).map(UserMapper::toDomain).orElse(null)

    override fun findAll(): List<User> =
        springDataRepo.findAll().map(UserMapper::toDomain)

    override fun findAll(pageable: Pageable): Page<User> {
        val springPageable = PageableMapper.toSpringPageable(pageable)
        val springPage = springDataRepo.findAll(springPageable)
        return PageableMapper.toDomainPage(springPage, UserMapper::toDomain)
    }

    override fun delete(id: UUID) =
        springDataRepo.deleteById(id)
}