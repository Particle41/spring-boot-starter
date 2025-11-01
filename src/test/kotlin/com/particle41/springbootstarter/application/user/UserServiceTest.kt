package com.particle41.springbootstarter.application.user

import com.particle41.springbootstarter.domain.pagination.Page
import com.particle41.springbootstarter.domain.pagination.Pageable
import com.particle41.springbootstarter.domain.user.exception.UserNotFoundException
import com.particle41.springbootstarter.domain.user.model.User
import com.particle41.springbootstarter.domain.user.repository.UserRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDateTime
import java.util.UUID

class UserServiceTest {

    @Mock
    lateinit var userRepository: UserRepository

    @InjectMocks
    lateinit var userService: UserServiceImpl

    private lateinit var sampleUser: User
    private val sampleId = UUID.randomUUID()
    private val now = LocalDateTime.now()

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        sampleUser = User(
            id = sampleId,
            name = "John Doe",
            email = "john@example.com",
            createdAt = now,
            updatedAt = now
        )
    }

    @Test
    fun `fetchOne returns user when found`() {
        whenever(userRepository.findById(sampleId)).thenReturn(sampleUser)

        val result = userService.fetchOne(sampleId)

        Assertions.assertEquals(sampleUser, result)
    }

    @Test
    fun `fetchOne throws UserNotFoundException when not found`() {
        whenever(userRepository.findById(sampleId)).thenReturn(null)

        Assertions.assertThrows(UserNotFoundException::class.java) {
            userService.fetchOne(sampleId)
        }
    }

    @Test
    fun `fetchAll returns list of users`() {
        val secondUser = sampleUser.copy(id = UUID.randomUUID())
        val users = listOf(sampleUser, secondUser)
        whenever(userRepository.findAll()).thenReturn(users)

        val result = userService.fetchAll()

        Assertions.assertEquals(2, result.size)
        Assertions.assertEquals(users, result)
    }

    @Test
    fun `fetchAll with pageable returns page of users`() {
        val secondUser = sampleUser.copy(id = UUID.randomUUID())
        val users = listOf(sampleUser, secondUser)
        val pageable = Pageable(page = 0, size = 10)
        val page = Page(
            content = users,
            pageNumber = 0,
            pageSize = 10,
            totalElements = 2L,
            totalPages = 1,
            isFirst = true,
            isLast = true,
            hasNext = false,
            hasPrevious = false
        )
        whenever(userRepository.findAll(pageable)).thenReturn(page)

        val result = userService.fetchAll(pageable)

        Assertions.assertEquals(2, result.content.size)
        Assertions.assertEquals(users, result.content)
        Assertions.assertEquals(0, result.pageNumber)
        Assertions.assertEquals(10, result.pageSize)
        Assertions.assertEquals(2L, result.totalElements)
        Assertions.assertEquals(1, result.totalPages)
        Assertions.assertTrue(result.isFirst)
        Assertions.assertTrue(result.isLast)
        Assertions.assertFalse(result.hasNext)
        Assertions.assertFalse(result.hasPrevious)
    }

    @Test
    fun `fetchAll with pageable returns empty page when no users`() {
        val pageable = Pageable(page = 0, size = 10)
        val emptyPage = Page<User>(
            content = emptyList(),
            pageNumber = 0,
            pageSize = 10,
            totalElements = 0L,
            totalPages = 0,
            isFirst = true,
            isLast = true,
            hasNext = false,
            hasPrevious = false
        )
        whenever(userRepository.findAll(pageable)).thenReturn(emptyPage)

        val result = userService.fetchAll(pageable)

        Assertions.assertEquals(0, result.content.size)
        Assertions.assertEquals(0L, result.totalElements)
    }

    @Test
    fun `create saves and returns user`() {
        whenever(userRepository.save(sampleUser)).thenReturn(sampleUser)

        val result = userService.create(sampleUser)

        Assertions.assertEquals(sampleUser, result)
        verify(userRepository).save(sampleUser)
    }

    @Test
    fun `update modifies and saves user`() {
        val updatedUser = sampleUser.copy(name = "Jane Doe")
        whenever(userRepository.findById(sampleId)).thenReturn(sampleUser)
        whenever(userRepository.save(any())).thenReturn(updatedUser)

        val inputUser = User(
            id = UUID.randomUUID(),
            name = "Jane Doe",
            email = "john@example.com",
            createdAt = now,
            updatedAt = now
        )
        val result = userService.update(sampleId, inputUser)

        Assertions.assertEquals("Jane Doe", result.name)
        Assertions.assertEquals(sampleUser.email, result.email)
        verify(userRepository).save(any())
    }

    @Test
    fun `update throws UserNotFoundException if user not found`() {
        whenever(userRepository.findById(sampleId)).thenReturn(null)

        Assertions.assertThrows(UserNotFoundException::class.java) {
            userService.update(sampleId, sampleUser)
        }
    }

    @Test
    fun `delete removes user`() {
        whenever(userRepository.findById(sampleId)).thenReturn(sampleUser)

        userService.delete(sampleId)

        verify(userRepository).delete(sampleId)
    }

    @Test
    fun `delete throws UserNotFoundException if user not found`() {
        whenever(userRepository.findById(sampleId)).thenReturn(null)

        Assertions.assertThrows(UserNotFoundException::class.java) {
            userService.delete(sampleId)
        }
    }
}