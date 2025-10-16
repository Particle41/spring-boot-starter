package com.particle41.springbootstarter.modules.user.application
import com.particle41.springbootstarter.modules.user.domain.model.User
import com.particle41.springbootstarter.modules.user.domain.repository.UserRepository
import com.particle41.springbootstarter.modules.user.domain.exception.UserNotFoundException
import org.junit.jupiter.api.Assertions.*
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

        assertEquals(sampleUser, result)
    }

    @Test
    fun `fetchOne throws UserNotFoundException when not found`() {
        whenever(userRepository.findById(sampleId)).thenReturn(null)

        assertThrows(UserNotFoundException::class.java) {
            userService.fetchOne(sampleId)
        }
    }

    @Test
    fun `fetchAll returns list of users`() {
        val secondUser = sampleUser.copy(id = UUID.randomUUID())
        val users = listOf(sampleUser, secondUser)
        whenever(userRepository.findAll()).thenReturn(users)

        val result = userService.fetchAll()

        assertEquals(2, result.size)
        assertEquals(users, result)
    }

    @Test
    fun `create saves and returns user`() {
        whenever(userRepository.save(sampleUser)).thenReturn(sampleUser)

        val result = userService.create(sampleUser)

        assertEquals(sampleUser, result)
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

        assertEquals("Jane Doe", result.name)
        assertEquals(sampleUser.email, result.email)
        verify(userRepository).save(any())
    }

    @Test
    fun `update throws UserNotFoundException if user not found`() {
        whenever(userRepository.findById(sampleId)).thenReturn(null)

        assertThrows(UserNotFoundException::class.java) {
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

        assertThrows(UserNotFoundException::class.java) {
            userService.delete(sampleId)
        }
    }
}
