package com.particle41.springbootstarter.presentation.user.controller

import com.particle41.springbootstarter.application.user.UserService
import com.particle41.springbootstarter.presentation.user.dto.UserRequest
import com.particle41.springbootstarter.presentation.user.dto.UserResponse
import com.particle41.springbootstarter.presentation.user.mapper.UserMapper
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService,
    private val userMapper: UserMapper
) {

    @PostMapping
    fun create(@Valid @RequestBody request: UserRequest): ResponseEntity<UserResponse> {
        val user = userMapper.toDomain(request)
        val createdUser = userService.create(user)
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toResponse(createdUser))
    }

    @GetMapping("/{id}")
    fun fetchOne(@PathVariable id: UUID): ResponseEntity<UserResponse> {
        val user = userService.fetchOne(id)
        return ResponseEntity.ok(userMapper.toResponse(user))
    }

    @GetMapping
    fun fetchAll(): ResponseEntity<List<UserResponse>> {
        val users = userService.fetchAll()
        return ResponseEntity.ok(users.map(userMapper::toResponse))
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @Valid @RequestBody request: UserRequest
    ): ResponseEntity<UserResponse> {
        val user = userMapper.toDomain(request)
        val updatedUser = userService.update(id, user)
        return ResponseEntity.ok(userMapper.toResponse(updatedUser))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): ResponseEntity<Void> {
        userService.delete(id)
        return ResponseEntity.noContent().build()
    }
}
