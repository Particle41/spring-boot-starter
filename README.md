# spring-boot-starter

This file provides guidance while working with code in this repository.

## Quick Commands

### Setup
- **Build project**: `./gradlew clean build`
- **Run locally**: `./gradlew bootRun`
- **Run with specific profile**: `./gradlew bootRun --args='--spring.profiles.active=local'`
- **Run tests**: `./gradlew test`
- **Run integration tests**: `./gradlew integrationTest`

### Development
- **Start dev server**: `./gradlew bootRun --args='--spring.profiles.active=dev'`
- **Start with debug port (5005)**: `./gradlew bootRun --debug-jvm`
- **Build jar**: `./gradlew clean bootJar -x test`
- **Run jar**: `java -jar build/libs/<app-name>.jar --spring.profiles.active=prod`

### Code Quality
- **Checkstyle**: `./gradlew checkstyleMain checkstyleTest`
- **Spotless (format code)**: `./gradlew spotlessApply`
- **Lint**: `./gradlew pmdMain pmdTest`
- **Test with coverage**: `./gradlew test jacocoTestReport`

---

## Architecture Overview

### Clean Architecture Flow
This codebase implements **Clean Architecture** with strict dependency rules.  
Request flow follows: **Controller → Service → Repository → Entity/DB**.

### Package Structure
- **Domain Layer** (`com.particle41.springbootstarter.feature.user.domain`)
    - Contains models, repository interfaces, domain exceptions
    - Must be framework-agnostic
- **Application Layer** (`com.particle41.springbootstarter.feature.user.application`)
    - Contains business logic, orchestrates operations
    - Uses domain models and repository interfaces
- **Infrastructure Layer** (`com.particle41.springbootstarter.feature.user.infrastructure`)
    - Contains JPA entities, repository implementations
    - Handles persistence, database interactions
- **Presentation Layer** (`com.particle41.springbootstarter.feature.user.presentation`)
    - Handles REST APIs, request/response DTOs, validation
    - Delegates to application services

---

## 🏛️ CLEAN ARCHITECTURE RULES

```
┌─────────────────────┐
│  PRESENTATION       │  ← Controllers, DTOs
├─────────────────────┤
│  APPLICATION        │  ← Services
├─────────────────────┤
│  INFRASTRUCTURE     │  ← Persistence, DB
├─────────────────────┤
│  DOMAIN             │  ← Core Models
└─────────────────────┘
```

**Dependency Rule**: `Presentation → Application → Infrastructure → Domain`  
**NEVER**: Inner layers depending on outer layers!

---

### Layer Responsibilities

#### 🎯 Domain Layer
- Models, repository interfaces, domain exceptions
- No dependencies on Spring, JPA, or DTOs

#### 🔧 Infrastructure Layer
- JPA entities, Spring Data repositories
- Mappers: Domain ↔ Persistence

#### 🎮 Application Layer
- Services interface and implementations
- Always works with domain objects

#### 🌐 Presentation Layer
- Controllers, request/response DTOs, validation
- Maps DTOs to/from domain models via mappers

---

## ✅ Example Imports

```java
// ✅ Controller → Service → Domain
import com.particle41.springbootstarter.feature.user.application.UserService;
import com.particle41.springbootstarter.feature.user.domain.User;

// ✅ Service → Domain
import com.particle41.springbootstarter.feature.user.domain.UserRepository;
```

```java
// ❌ WRONG: Domain importing Controller
import com.particle41.springbootstarter.feature.user.presentation.controller.UserController;
```

---

## Validation Rules

- Use **Bean Validation (`javax.validation` / `jakarta.validation`)** in DTOs
- Example:

```java
public class UserRequest {
    @NotBlank(message = "Name must not be blank")
    private String name;

    @Email(message = "Invalid email format")
    private String email;
}
```

- Handle validation errors globally in `@RestControllerAdvice`

---

## Development Patterns

### Adding a New Feature
1. **Domain Layer**: Create model, repository interface, exceptions
2. **Application Layer**: Create service
3. **Infrastructure Layer**: Create JPA entity & repository implementation
4. **Presentation Layer**: Create controller, DTOs, and mappers

### Mappers
- **Presentation Mappers**: Domain ↔ DTO
- **Infrastructure Mappers**: Domain ↔ JPA Entity
- Always use dedicated mapper classes, never inline conversions in controllers

---

## Naming Conventions
- **Classes**: `UserService`, `UserController`, `UserEntity`, `UserMapper`
- **Interfaces**: `UserRepository`
- **Exceptions**: Suffix with `Exception` (`UserNotFoundException`)
- **DTOs**: Suffix with `Request` / `Response` (`UserRequest`, `UserResponse`)
- **Tests**: Suffix with `Test` (`UserServiceTest`, `UserControllerTest`)

---

## Testing Strategy
- **Unit tests**: Services, domain models
- **Integration tests**: Repository & DB
- **E2E tests**: Controllers (MockMvc/TestRestTemplate)
- **Coverage target**: >80% for business logic

---

## Error Handling
- Use domain-specific exceptions (`UserNotFoundException`)
- Map exceptions to HTTP codes in `GlobalExceptionHandler` (`@RestControllerAdvice`)

---

## Git Workflow & Commit Requirements
- Follow conventional commits:
  ```
  feat(user): add user creation API
  fix(user): fix email validation bug
  test(user): add unit test for UserService
  ```
- Run tests and formatting before pushing:
  ```bash
  gradle spotless:apply test
  ```

---

## Checklist Before Merge
- [ ] Follows Clean Architecture rules
- [ ] DTOs validated with `@Valid` annotations
- [ ] No inline DTO construction in controllers
- [ ] Mapper classes in place and tested
- [ ] Unit & integration tests passing
- [ ] Code formatted (`spotless:apply`)