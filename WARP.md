# Spring Boot Starter - Warp Documentation

A comprehensive Spring Boot application built with Kotlin following Clean Architecture principles, featuring a complete User management system with REST API, JPA integration, and comprehensive testing.

## 🏗️ Architecture Overview

This application implements **Clean Architecture** with the following layers:

- **Presentation Layer**: REST controllers, DTOs, and presentation mappers
- **Application Layer**: Business services
- **Domain Layer**: Core business entities, domain models, and repository interfaces
- **Infrastructure Layer**: JPA entities, repository implementations, and external integrations

### Project Structure

```
src/
├── main/kotlin/com/particle41/springbootstarter/
│   ├── AppApplication.kt                              # Main Spring Boot application
│   ├── application/user/
│   │   ├── UserService.kt                            # Service interface
│   │   └── UserServiceImpl.kt                        # Service implementation
│   ├── domain/
│   │   ├── pagination/                               # Pagination domain models
│   │   │   ├── Pageable.kt                           # Domain pageable
│   │   │   └── Page.kt                               # Domain page result
│   │   └── user/
│   │       ├── model/User.kt                         # Core domain model
│   │       ├── repository/UserRepository.kt          # Repository interface
│   │       └── exception/UserNotFoundException.kt    # Domain-specific exceptions
│   ├── infrastructure/
│   │   ├── pagination/mapper/PageableMapper.kt       # Spring pagination mapper
│   │   └── user/
│   │       ├── entity/UserEntity.kt                  # JPA entity
│   │       ├── mapper/UserMapper.kt                  # Infrastructure mapper
│   │       └── repository/
│   │           ├── SpringDataUserRepository.kt       # Spring Data JPA interface
│   │           └── UserRepositoryImpl.kt             # Repository implementation
│   ├── presentation/
│   │   ├── pagination/                               # Pagination DTOs
│   │   │   ├── dto/
│   │   │   │   ├── PageRequest.kt                    # Pagination request DTO
│   │   │   │   └── PageResponse.kt                   # Pagination response DTO
│   │   │   └── mapper/PageableMapper.kt              # Presentation pagination mapper
│   │   └── user/
│   │       ├── controller/UserController.kt          # REST controller
│   │       ├── dto/
│   │       │   ├── UserRequest.kt                    # Input DTO with validation
│   │       │   └── UserResponse.kt                   # Output DTO
│   │       └── mapper/UserMapper.kt                  # Presentation mapper
│   └── shared/
│       ├── annotations/ApiVersion.kt                 # API versioning annotation
│       ├── config/ApiVersionConfig.kt                # API versioning configuration
│       ├── advices/GlobalExceptionHandler.kt         # Global error handling
│       └── exceptions/
│           ├── DomainException.kt                    # Base domain exception
│           └── DomainNotFoundException.kt            # Base not found exception
├── test/kotlin/com/particle41/springbootstarter/
│   ├── AppApplicationTests.kt                        # Application tests
│   └── application/user/UserServiceTest.kt           # Unit tests
└── integrationTest/kotlin/com/particle41/springbootstarter/
    └── presentation/user/controller/
        └── UserControllerIntegrationTest.kt          # Integration tests
```

## 📊 Core Components

### Domain Model

```kotlin path=/Users/aniruddh/Documents/projects/springboot/src/spring-boot-starter/src/main/kotlin/com/particle41/springbootstarter/domain/user/model/User.kt start=6
data class User(
    val id: UUID,
    val name: String,
    val email: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
```

**Key Features:**
- Immutable data class following domain-driven design
- UUID-based identification for distributed systems
- Automatic timestamp management

### REST API Controller

```kotlin path=/Users/aniruddh/Documents/projects/springboot/src/spring-boot-starter/src/main/kotlin/com/particle41/springbootstarter/presentation/user/controller/UserController.kt start=20
@PostMapping
fun create(@Valid @RequestBody request: UserRequest): ResponseEntity<UserResponse> {
    val user = userMapper.toDomain(request)
    val createdUser = userService.create(user)
    return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toResponse(createdUser))
}
```

**API Endpoints:**
- `POST /api/v1/users` - Create new user
- `GET /api/v1/users/{id}` - Fetch user by ID
- `GET /api/v1/users?page=0&size=10` - Fetch paginated users
- `PUT /api/v1/users/{id}` - Update existing user
- `DELETE /api/v1/users/{id}` - Delete user

### Business Service Layer

```kotlin path=/Users/aniruddh/Documents/projects/springboot/src/spring-boot-starter/src/main/kotlin/com/particle41/springbootstarter/application/user/UserServiceImpl.kt start=16
override fun fetchOne(id: UUID): User =
    userRepository.findById(id) ?: throw UserNotFoundException(id.toString())

@Transactional
override fun update(id: UUID, user: User): User {
    val existingUser = userRepository.findById(id) ?: throw UserNotFoundException(id.toString())
    val updatedUser = existingUser.copy(
        name = user.name,
        email = user.email,
        updatedAt = LocalDateTime.now()
    )
    return userRepository.save(updatedUser)
}
```

**Features:**
- Transactional operations for data consistency
- Domain exception handling
- Business logic encapsulation

### Data Validation

```kotlin path=/Users/aniruddh/Documents/projects/springboot/src/spring-boot-starter/src/main/kotlin/com/particle41/springbootstarter/presentation/user/dto/UserRequest.kt start=6
data class UserRequest(
    @field:NotBlank(message = "Name must not be blank")
    val name: String,

    @field:NotBlank(message = "Email must not be blank")
    @field:Email(message = "Invalid email format")
    val email: String
)
```

**Validation Rules:**
- Name: Cannot be blank
- Email: Must be valid email format and not blank

## 🔢 API Versioning

This application uses a custom annotation-based approach for API versioning, allowing clean and maintainable version management.

### Custom @ApiVersion Annotation

```kotlin path=/Users/aniruddh/Documents/projects/springboot/src/spring-boot-starter/src/main/kotlin/com/particle41/springbootstarter/shared/annotations/ApiVersion.kt start=1
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class ApiVersion(val value: Int)
```

### Usage in Controllers

```kotlin
@RestController
@ApiVersion(1)
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
    private val userMapper: UserMapper
) {
    // Controller methods
}
```

**How it Works:**
- Add `@ApiVersion(1)` to any controller class
- All endpoints in that controller automatically get prefixed with `/api/v{version}`
- Example: `@RequestMapping("/users")` with `@ApiVersion(1)` becomes `/api/v1/users`

### Custom Request Mapping Handler

```kotlin path=/Users/aniruddh/Documents/projects/springboot/src/spring-boot-starter/src/main/kotlin/com/particle41/springbootstarter/shared/config/ApiVersionConfig.kt start=14
@Bean
fun apiVersionRequestMappingHandlerMapping(): RequestMappingHandlerMapping {
    return object : RequestMappingHandlerMapping() {
        override fun getMappingForMethod(method: Method, handlerType: Class<*>): RequestMappingInfo? {
            val info = super.getMappingForMethod(method, handlerType) ?: return null
            val apiVersion = AnnotatedElementUtils.findMergedAnnotation(handlerType, ApiVersion::class.java)
                ?: return info
            val versionPath = "/api/v${apiVersion.value}"
            val versionInfo = RequestMappingInfo.paths(versionPath).build()
            return versionInfo.combine(info)
        }
    }.apply {
        order = 0
    }
}
```

**Benefits:**
- Clean, declarative versioning at the controller level
- No need to repeat `/api/v1` in every `@RequestMapping`
- Easy to create new API versions by changing the annotation value
- Type-safe with compile-time checking

## 📄 Pagination

The application implements a clean, framework-independent pagination system following Clean Architecture principles.

### Pagination Architecture

#### Domain Layer (Framework-Independent)

```kotlin path=/Users/aniruddh/Documents/projects/springboot/src/spring-boot-starter/src/main/kotlin/com/particle41/springbootstarter/domain/pagination/Pageable.kt start=3
data class Pageable(
    val page: Int,
    val size: Int,
    val sort: Sort? = null
) {
    init {
        require(page >= 0) { "Page index must not be less than zero" }
        require(size > 0) { "Page size must be greater than zero" }
    }
}

data class Sort(
    val property: String,
    val direction: Direction = Direction.ASC
)

enum class Direction {
    ASC, DESC
}
```

```kotlin path=/Users/aniruddh/Documents/projects/springboot/src/spring-boot-starter/src/main/kotlin/com/particle41/springbootstarter/domain/pagination/Page.kt start=3
data class Page<T>(
    val content: List<T>,
    val pageNumber: Int,
    val pageSize: Int,
    val totalElements: Long,
    val totalPages: Int,
    val isFirst: Boolean,
    val isLast: Boolean,
    val hasNext: Boolean,
    val hasPrevious: Boolean
)
```

#### Presentation Layer DTOs

```kotlin path=/Users/aniruddh/Documents/projects/springboot/src/spring-boot-starter/src/main/kotlin/com/particle41/springbootstarter/presentation/pagination/dto/PageRequest.kt start=3
data class PageRequest(
    val page: Int,
    val size: Int
)
```

```kotlin path=/Users/aniruddh/Documents/projects/springboot/src/spring-boot-starter/src/main/kotlin/com/particle41/springbootstarter/presentation/pagination/dto/PageResponse.kt start=3
data class PageResponse<T>(
    val content: List<T>,
    val pageNumber: Int,
    val pageSize: Int,
    val totalElements: Long,
    val totalPages: Int,
    val isFirst: Boolean,
    val isLast: Boolean,
    val hasNext: Boolean,
    val hasPrevious: Boolean
)
```

### Pagination Flow

```
HTTP Request: GET /api/v1/users?page=0&size=10
  ↓
  UserController receives page & size parameters
  ↓
  Creates PageRequest(page=0, size=10)
  ↓
  PageableMapper.toPageable() → Pageable (domain)
  ↓
  UserService.fetchAll(pageable)
  ↓
  UserRepository.findAll(pageable)
  ↓
  PageableMapper.toSpringPageable() → Spring Pageable
  ↓
  Spring Data JPA executes paginated query
  ↓
  PageableMapper.toDomainPage() → Page<User> (domain)
  ↓
  PageableMapper.toResponse() → PageResponse<UserResponse>
  ↓
HTTP Response: JSON with paginated data
```

### Controller Usage

```kotlin path=/Users/aniruddh/Documents/projects/springboot/src/spring-boot-starter/src/main/kotlin/com/particle41/springbootstarter/presentation/user/controller/UserController.kt start=38
@GetMapping
fun fetchAll(
    @RequestParam page: Int,
    @RequestParam size: Int
): ResponseEntity<PageResponse<UserResponse>> {
    val pageRequest = PageRequest(page = page, size = size)
    val pageable = PageableMapper.toPageable(pageRequest)
    val userPage = userService.fetchAll(pageable)
    val response = PageableMapper.toResponse(userPage, userMapper::toResponse)
    return ResponseEntity.ok(response)
}
```

### Example API Request/Response

**Request:**
```bash
GET /api/v1/users?page=0&size=10
```

**Response:**
```json
{
  "content": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "name": "John Doe",
      "email": "john@example.com",
      "createdAt": "2025-01-01T10:00:00",
      "updatedAt": "2025-01-01T10:00:00"
    }
  ],
  "pageNumber": 0,
  "pageSize": 10,
  "totalElements": 1,
  "totalPages": 1,
  "isFirst": true,
  "isLast": true,
  "hasNext": false,
  "hasPrevious": false
}
```

### Pagination Mappers

#### Presentation Layer Mapper

```kotlin
object PageableMapper {
    fun toPageable(pageRequest: PageRequest): Pageable {
        return Pageable(
            page = pageRequest.page,
            size = pageRequest.size
        )
    }

    fun <T, R> toResponse(page: Page<T>, contentMapper: (T) -> R): PageResponse<R> {
        return PageResponse(
            content = page.content.map(contentMapper),
            pageNumber = page.pageNumber,
            pageSize = page.pageSize,
            totalElements = page.totalElements,
            totalPages = page.totalPages,
            isFirst = page.isFirst,
            isLast = page.isLast,
            hasNext = page.hasNext,
            hasPrevious = page.hasPrevious
        )
    }
}
```

#### Infrastructure Layer Mapper

```kotlin
object PageableMapper {
    fun toSpringPageable(pageable: DomainPageable): SpringPageable {
        val sort = pageable.sort?.let {
            val direction = if (it.direction == Direction.ASC) {
                SpringSort.Direction.ASC
            } else {
                SpringSort.Direction.DESC
            }
            SpringSort.by(direction, it.property)
        } ?: SpringSort.unsorted()

        return org.springframework.data.domain.PageRequest.of(
            pageable.page,
            pageable.size,
            sort
        )
    }

    fun <T, R> toDomainPage(
        springPage: org.springframework.data.domain.Page<T>,
        contentMapper: (T) -> R
    ): Page<R> {
        return Page(
            content = springPage.content.map(contentMapper),
            pageNumber = springPage.number,
            pageSize = springPage.size,
            totalElements = springPage.totalElements,
            totalPages = springPage.totalPages,
            isFirst = springPage.isFirst,
            isLast = springPage.isLast,
            hasNext = springPage.hasNext(),
            hasPrevious = springPage.hasPrevious()
        )
    }
}
```

### Repository Implementation

```kotlin path=/Users/aniruddh/Documents/projects/springboot/src/spring-boot-starter/src/main/kotlin/com/particle41/springbootstarter/infrastructure/user/repository/UserRepositoryImpl.kt start=29
override fun findAll(pageable: Pageable): Page<User> {
    val springPageable = PageableMapper.toSpringPageable(pageable)
    val springPage = springDataRepo.findAll(springPageable)
    return PageableMapper.toDomainPage(springPage, UserMapper::toDomain)
}
```

**Key Benefits:**
- **Framework Independence**: Domain layer uses its own `Pageable` and `Page` types
- **Clean Separation**: Each layer has its own pagination models
- **Testability**: Easy to test with mocked pagination without Spring dependencies
- **Flexibility**: Can easily swap Spring Data JPA with another framework
- **Type Safety**: Compile-time checking for all pagination parameters

## 🗄️ Database Configuration

### H2 In-Memory Database

```properties path=/Users/aniruddh/Documents/projects/springboot/src/spring-boot-starter/src/main/resources/application.properties start=1
spring.application.name=springbootstarter
# H2 database config
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect
# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

### JPA Entity

```kotlin path=/Users/aniruddh/Documents/projects/springboot/src/spring-boot-starter/src/main/kotlin/com/particle41/springbootstarter/infrastructure/user/entity/UserEntity.kt start=16
@Entity
@Table(name = "users")
data class UserEntity(
    @Id
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false, unique = true)
    var email: String,

    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
)
```

**Database Features:**
- Automatic UUID generation
- Email uniqueness constraint
- Timestamp management with JPA lifecycle hooks
- H2 console available at `/h2-console`

## 🔧 Build Configuration

### Gradle (Kotlin DSL)

```kotlin path=/Users/aniruddh/Documents/projects/springboot/src/spring-boot-starter/build.gradle.kts start=1
plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.5.3"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("plugin.jpa") version "1.9.25"
}

group = "com.particle41"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}
```

**Key Dependencies:**
- Spring Boot 3.5.3
- Spring Data JPA
- Spring Boot Actuator
- H2 Database
- Jackson Kotlin Module
- Bean Validation (jakarta.validation)

## 🧪 Testing Strategy

### Unit Tests

```kotlin path=/Users/aniruddh/Documents/projects/springboot/src/spring-boot-starter/src/test/kotlin/com/particle41/springbootstarter/application/user/UserServiceTest.kt start=41
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
```

**Unit Testing Features:**
- Mockito with Kotlin extensions
- JUnit 5 test framework
- Complete service layer coverage
- Exception scenario testing

### Integration Tests

```kotlin path=/Users/aniruddh/Documents/projects/springboot/src/spring-boot-starter/src/integrationTest/kotlin/com/particle41/springbootstarter/presentation/user/controller/UserControllerIntegrationTest.kt start=36
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
        .andExpected(jsonPath("$.name").value("Alice"))
        .andExpect(jsonPath("$.email").value("alice@example.com"))
}
```

**Integration Testing Features:**
- End-to-end API testing with MockMvc
- Full Spring context loading
- Database integration testing
- Validation error testing
- HTTP status code verification

### Test Configuration

```kotlin path=/Users/aniruddh/Documents/projects/springboot/src/spring-boot-starter/build.gradle.kts start=54
sourceSets {
    val integrationTest by creating {
        kotlin.srcDir("src/integrationTest/kotlin")
        resources.srcDir("src/integrationTest/resources")
        compileClasspath += sourceSets["main"].output + configurations["testRuntimeClasspath"]
        runtimeClasspath += output + compileClasspath
    }
}

tasks.register<Test>("integrationTest") {
    description = "Runs integration tests"
    group = "verification"
    testClassesDirs = sourceSets["integrationTest"].output.classesDirs
    classpath = sourceSets["integrationTest"].runtimeClasspath
    shouldRunAfter(tasks.test)
}
```

## 🚀 Quick Commands

### Development Workflow

```bash
# Setup and Build
./gradlew clean build          # Clean and build project
./gradlew bootRun             # Run application locally
./gradlew bootRun --args='--spring.profiles.active=local'  # Run with specific profile

# Testing
./gradlew test                # Run unit tests
./gradlew integrationTest     # Run integration tests
./gradlew check              # Run all tests and quality checks
```

### Development URLs

- **Application**: http://localhost:8080
- **API Base**: http://localhost:8080/api/v1
- **Users Endpoint**: http://localhost:8080/api/v1/users
- **Paginated Users**: http://localhost:8080/api/v1/users?page=0&size=10
- **H2 Console**: http://localhost:8080/h2-console
- **Actuator Health**: http://localhost:8080/actuator/health

## 🔍 Error Handling

### Exception Hierarchy

```kotlin path=/Users/aniruddh/Documents/projects/springboot/src/spring-boot-starter/src/main/kotlin/com/particle41/springbootstarter/shared/exceptions/DomainException.kt start=7
abstract class DomainException(
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)
```

```kotlin path=/Users/aniruddh/Documents/projects/springboot/src/spring-boot-starter/src/main/kotlin/com/particle41/springbootstarter/shared/exceptions/DomainNotFoundException.kt start=6
abstract class DomainNotFoundException(
    entityName: String,
    identifier: String,
    cause: Throwable? = null
) : DomainException("$entityName with id=$identifier not found", cause)
```

### Global Exception Handler

```kotlin path=/Users/aniruddh/Documents/projects/springboot/src/spring-boot-starter/src/main/kotlin/com/particle41/springbootstarter/shared/advices/GlobalExceptionHandler.kt start=14
@ExceptionHandler(DomainNotFoundException::class)
fun handleDomainNotFound(ex: DomainNotFoundException): ResponseEntity<String> {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.message)
}

@ExceptionHandler(DomainException::class)
fun handleDomainException(ex: DomainException): ResponseEntity<String> {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.message)
}

@ExceptionHandler(MethodArgumentNotValidException::class)
fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, String?>> {
    val errors = ex.bindingResult.fieldErrors.associate { error ->
        error.field to error.defaultMessage
    }
    return ResponseEntity.badRequest().body(errors)
}
```

**Error Handling Features:**
- Hierarchical domain exception handling
- Bean validation error mapping
- Consistent HTTP status codes
- Structured error responses

## 📋 Development Standards

### Code Quality Checklist

- [ ] **Clean Architecture**: Proper layer separation maintained
- [ ] **Validation**: All DTOs use `@Valid` annotations
- [ ] **Mapping**: No inline DTO construction in controllers
- [ ] **Testing**: Unit and integration tests covering all scenarios
- [ ] **Formatting**: Code properly formatted and consistent
- [ ] **Transactions**: Database operations properly transactional
- [ ] **Exception Handling**: Domain exceptions properly propagated

### Architecture Principles

1. **Dependency Inversion**: Domain layer has no dependencies on infrastructure
2. **Single Responsibility**: Each class has one clear responsibility
3. **Interface Segregation**: Clean interfaces for repository and service layers
4. **Immutable Domain**: Domain models are immutable data classes
5. **Fail Fast**: Input validation at API boundaries
6. **Separation of Concerns**: Clear boundaries between layers

## 🔧 Technology Stack

- **Language**: Kotlin 1.9.25
- **Framework**: Spring Boot 3.5.3
- **Database**: H2 (in-memory)
- **ORM**: Spring Data JPA with Hibernate
- **Testing**: JUnit 5, Mockito, MockMvc
- **Build**: Gradle 8.14.2 with Kotlin DSL
- **Java Version**: 17

## 🎯 Next Steps

To extend this application:

1. **Add More Entities**: Follow the same modular structure
2. **External Database**: Replace H2 with PostgreSQL/MySQL
3. **Security**: Add Spring Security with JWT authentication
4. **Documentation**: Integrate OpenAPI/Swagger
5. **Monitoring**: Enhance Actuator with custom metrics
6. **Caching**: Add Redis/Caffeine caching layer
7. **Messaging**: Integrate with RabbitMQ/Kafka

This Spring Boot starter provides a solid foundation for building scalable, maintainable applications following industry best practices.