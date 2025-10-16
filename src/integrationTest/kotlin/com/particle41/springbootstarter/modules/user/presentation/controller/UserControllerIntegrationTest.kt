package com.particle41.springbootstarter.modules.user.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.particle41.springbootstarter.modules.user.application.UserService
import com.particle41.springbootstarter.modules.user.domain.model.User
import com.particle41.springbootstarter.modules.user.presentation.dto.UserRequest
import com.particle41.springbootstarter.modules.user.presentation.mapper.UserMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var userMapper: UserMapper

    @Test
    fun `POST create user returns 201`() {
        val request = UserRequest(name = "Alice", email = "alice@example.com")
        val json = objectMapper.writeValueAsString(request)

        mockMvc.perform(
            post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.name").value("Alice"))
            .andExpect(jsonPath("$.email").value("alice@example.com"))
    }

    @Test
    fun `GET fetchOne returns user`() {
        val user = userService.create(User(name = "Bob", email = "bob@example.com"))

        mockMvc.perform(get("/api/users/${user.id}"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Bob"))
            .andExpect(jsonPath("$.email").value("bob@example.com"))
    }

    @Test
    fun `GET fetchAll returns list of users`() {
        userService.create(User(name = "Charlie", email = "charlie@example.com"))

        mockMvc.perform(get("/api/users"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[*].name").value(org.hamcrest.Matchers.hasItems("Charlie", "Bob", "Alice")))
    }

    @Test
    fun `PUT update user returns updated user`() {
        val user = userService.create(User(name = "Dave", email = "dave@example.com"))
        val request = UserRequest(name = "David", email = "david@example.com")
        val json = objectMapper.writeValueAsString(request)

        mockMvc.perform(
            put("/api/users/${user.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("David"))
            .andExpect(jsonPath("$.email").value("david@example.com"))
    }

    @Test
    fun `DELETE user returns no content`() {
        val user = userService.create(User(name = "Eve", email = "eve@example.com"))

        mockMvc.perform(delete("/api/users/${user.id}"))
            .andExpect(status().isNoContent)
    }

    @Test
    fun `POST create user with blank name returns 400`() {
        val request = UserRequest(name = "", email = "valid@example.com")
        val json = objectMapper.writeValueAsString(request)

        mockMvc.perform(
            post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.name").value("Name must not be blank"))
    }

    @Test
    fun `POST create user with invalid email returns 400`() {
        val request = UserRequest(name = "Alice", email = "invalid-email")
        val json = objectMapper.writeValueAsString(request)

        mockMvc.perform(
            post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.email").value("Invalid email format"))
    }

    @Test
    fun `GET fetchOne with non-existing id returns 404`() {
        mockMvc.perform(get("/api/users/9999"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `PUT update user with blank fields returns 400`() {
        val email = "bob-${UUID.randomUUID()}@example.com"
        val user = userService.create(User(name = "Bob", email = email))
        val request = UserRequest(name = "", email = "")
        val json = objectMapper.writeValueAsString(request)

        mockMvc.perform(
            put("/api/users/${user.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.name").value("Name must not be blank"))
            .andExpect(jsonPath("$.email").value("Email must not be blank"))
    }

    @Test
    fun `DELETE non-existing user returns 404`() {
        mockMvc.perform(delete("/api/users/9999"))
            .andExpect(status().isNotFound)
    }

}