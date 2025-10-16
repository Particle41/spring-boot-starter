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

class UserServiceTest {

    @Mock
    lateinit var userRepository: UserRepository

    @InjectMocks
    lateinit var userService: UserServiceImpl

    private lateinit var sampleUser: User

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        sampleUser = User(id = 1L, name = "John Doe", email = "john@example.com")
    }

    @Test
    fun `fetchOne returns user when found`() {
        whenever(userRepository.findById(1L)).thenReturn(sampleUser)

        val result = userService.fetchOne(1L)

        assertEquals(sampleUser, result)
    }

    @Test
    fun `fetchOne throws UserNotFoundException when not found`() {
        whenever(userRepository.findById(1L)).thenReturn(null)

        assertThrows(UserNotFoundException::class.java) {
            userService.fetchOne(1L)
        }
    }

    @Test
    fun `fetchAll returns list of users`() {
        val users = listOf(sampleUser, sampleUser.copy(id = 2L))
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
        whenever(userRepository.findById(1L)).thenReturn(sampleUser)
        whenever(userRepository.save(any())).thenReturn(updatedUser)

        val result = userService.update(1L, User(name = "Jane Doe", email = "john@example.com"))

        assertEquals("Jane Doe", result.name)
        assertEquals(sampleUser.email, result.email)
        verify(userRepository).save(any())
    }

    @Test
    fun `update throws UserNotFoundException if user not found`() {
        whenever(userRepository.findById(1L)).thenReturn(null)

        assertThrows(UserNotFoundException::class.java) {
            userService.update(1L, sampleUser)
        }
    }

    @Test
    fun `delete removes user`() {
        whenever(userRepository.findById(1L)).thenReturn(sampleUser)

        userService.delete(1L)

        verify(userRepository).delete(1L)
    }

    @Test
    fun `delete throws UserNotFoundException if user not found`() {
        whenever(userRepository.findById(1L)).thenReturn(null)

        assertThrows(UserNotFoundException::class.java) {
            userService.delete(1L)
        }
    }
}
