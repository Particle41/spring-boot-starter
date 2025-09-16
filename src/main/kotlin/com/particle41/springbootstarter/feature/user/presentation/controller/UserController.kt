package com.particle41.springbootstarter.feature.user.presentation.controller

import com.particle41.springbootstarter.feature.user.application.UserService
import com.particle41.springbootstarter.feature.user.presentation.dto.UserRequest
import com.particle41.springbootstarter.feature.user.presentation.dto.UserResponse
import com.particle41.springbootstarter.feature.user.presentation.mapper.UserDtoMapper
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService,
    private val userDtoMapper: UserDtoMapper
) {

    @PostMapping
    fun create(@Valid @RequestBody request: UserRequest): ResponseEntity<UserResponse> {
        val user = userDtoMapper.toDomain(request)
        val createdUser = userService.create(user)
        return ResponseEntity.status(HttpStatus.CREATED).body(userDtoMapper.toResponse(createdUser))
    }

    @GetMapping("/{id}")
    fun fetchOne(@PathVariable id: Long): ResponseEntity<UserResponse> {
        val user = userService.fetchOne(id)
        return ResponseEntity.ok(userDtoMapper.toResponse(user))
    }

    @GetMapping
    fun fetchAll(): ResponseEntity<List<UserResponse>> {
        val users = userService.fetchAll()
        return ResponseEntity.ok(users.map(userDtoMapper::toResponse))
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody request: UserRequest
    ): ResponseEntity<UserResponse> {
        val user = userDtoMapper.toDomain(request)
        val updatedUser = userService.update(id, user)
        return ResponseEntity.ok(userDtoMapper.toResponse(updatedUser))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        userService.delete(id)
        return ResponseEntity.noContent().build()
    }
}
