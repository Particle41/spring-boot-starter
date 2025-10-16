# WARP.md

This file provides guidance to WARP (warp.dev) when working with code in this repository.

## Quick Commands

### Development & Testing
- **Build project**: `./gradlew clean build`
- **Run locally**: `./gradlew bootRun`
- **Run with debug (port 5005)**: `./gradlew bootRun --debug-jvm`
- **Run tests**: `./gradlew test`
- **Run integration tests**: `./gradlew integrationTest`
- **Run single test class**: `./gradlew test --tests "*UserServiceTest"`
- **Test with coverage**: `./gradlew test jacocoTestReport`

### Profiles & Environment
- **Dev profile**: `./gradlew bootRun --args='--spring.profiles.active=dev'`
- **Local profile**: `./gradlew bootRun --args='--spring.profiles.active=local'`
- **Build jar**: `./gradlew clean bootJar -x test`

### Code Quality 
- **Format code**: `./gradlew spotlessApply`
- **Checkstyle**: `./gradlew checkstyleMain checkstyleTest`
- **PMD lint**: `./gradlew pmdMain pmdTest`

## Architecture Overview

This Spring Boot Kotlin application implements **Clean Architecture** with 4 distinct layers and strict dependency rules.

### Clean Architecture Layers

```
┌─────────────────────┐
│  PRESENTATION       │  ← Controllers, DTOs, Validation
├─────────────────────┤
│  APPLICATION        │  ← Services, Use Cases  
├─────────────────────┤
│  INFRASTRUCTURE     │  ← JPA Entities, Repository Impl
├─────────────────────┤
│  DOMAIN             │  ← Core Models, Repository Interfaces
└─────────────────────┘
```

**Critical Rule**: Dependencies flow inward only. Domain layer has ZERO framework dependencies.

### Feature Organization

Each business domain follows this structure:
```
modules/{domain}/
├── domain/                    
│   ├── {Entity}.kt            # Domain models (framework-free)
│   ├── {Entity}Repository.kt  # Repository interfaces  
│   └── exception/             # Domain exceptions
├── application/               
│   ├── {Entity}Service.kt     # Service interfaces
│   └── {Entity}ServiceImpl.kt # Service implementations
├── infrastructure/            
│   ├── persistence/
│   │   ├── {Entity}Entity.kt       # JPA entities
│   │   ├── SpringData{Entity}Repository.kt # Spring Data JPA
│   │   └── {Entity}RepositoryImpl.kt # Domain repository impl
│   └── mapper/
│       └── {Entity}Mapper.kt       # Domain ↔ Entity mapping
└── presentation/              
    ├── controller/
    │   └── {Entity}Controller.kt   # REST endpoints
    ├── dto/
    │   ├── {Entity}Request.kt      # Input DTOs with validation
    │   └── {Entity}Response.kt     # Output DTOs
    └── mapper/
        └── {Entity}DtoMapper.kt    # Domain ↔ DTO mapping
```

### Current Implementation: User Feature

- **Domain**: `User.kt` (pure Kotlin data class), `UserRepository.kt` (interface)
- **Application**: `UserService.kt` + `UserServiceImpl.kt` with `@Transactional`
- **Infrastructure**: `UserEntity.kt` (@Entity), `SpringDataUserRepository.kt` (JpaRepository), `UserRepositoryImpl.kt`  
- **Presentation**: `UserController.kt` (@RestController), `UserRequest.kt`/`UserResponse.kt` (DTOs), `UserDtoMapper.kt`

## Key Patterns & Standards

### Domain Layer Rules
- **Pure Kotlin**: No Spring annotations, JPA annotations, or framework imports
- **Immutable Models**: Use `data class` with `val` properties
- **Repository Interfaces**: Define contracts without implementation details
- **Business Exceptions**: Extend `RuntimeException` with meaningful messages

### Infrastructure Layer Rules  
- **JPA Entities**: Use `@Entity`, `@Table`, `@Column` appropriately
- **Spring Data**: Extend `JpaRepository<Entity, ID>`
- **Repository Implementations**: Implement domain interfaces, use static mappers
- **Static Mappers**: `object UserMapper` with `toDomain()` and `toEntity()` methods

### Application Layer Rules
- **Service Interfaces + Implementations**: Separate concerns clearly
- **Transaction Management**: Use `@Transactional` for write operations
- **Exception Propagation**: Let domain exceptions bubble up to global handler
- **Business Logic**: Orchestrate domain objects and repository calls

### Presentation Layer Rules
- **REST Controllers**: Use `@RestController` and `@RequestMapping`
- **DTO Validation**: Apply `@Valid` with Jakarta validation annotations (`@NotBlank`, `@Email`)
- **HTTP Status Codes**: Return appropriate status (201 for create, 204 for delete, etc.)
- **Response Mapping**: Always convert domain objects to DTOs before returning

### Exception Handling
- **Global Handler**: `GlobalExceptionHandler` with `@RestControllerAdvice`
- **Domain Exceptions**: Create specific exceptions like `UserNotFoundException`
- **Validation Errors**: Handle `MethodArgumentNotValidException` for DTO validation
- **Standard Responses**: Consistent error response format

## Development Workflow

### Adding a New Feature Domain
1. **Domain Layer**: Create model, repository interface, domain exceptions
2. **Application Layer**: Create service interface and implementation with `@Transactional`
3. **Infrastructure Layer**: Create JPA entity, Spring Data repository, implementation, static mapper
4. **Presentation Layer**: Create controller, request/response DTOs, DTO mapper  
5. **Testing**: Unit tests for services, integration tests for controllers

### Testing Strategy
- **Unit Tests**: Mock dependencies, test business logic in isolation
- **Integration Tests**: Test full HTTP flow with `@SpringBootTest`
- **Test Structure**: Co-located with source code, use `Test` suffix
- **Mocking**: Use `@Mock` and `@InjectMocks` with MockitoAnnotations

### Database & Configuration
- **H2 In-Memory**: Default database for development/testing
- **JPA/Hibernate**: Auto DDL with `spring.jpa.hibernate.ddl-auto=update`
- **H2 Console**: Available at `/h2-console` for debugging
- **Profiles**: Support for `dev`, `local`, `prod` profiles

## Code Quality Requirements

### Kotlin Standards
- **Data Classes**: Use for immutable domain models and DTOs
- **Null Safety**: Leverage Kotlin's null safety, use `?` appropriately  
- **Extension Functions**: Use when appropriate for readability
- **Coroutines**: Consider for async operations (not currently used)

### Dependency Injection
- **Constructor Injection**: Standard Spring Boot pattern with `val` properties
- **Component Scanning**: Use `@Service`, `@Repository`, `@Component` appropriately
- **Interface Segregation**: Keep interfaces focused and cohesive

### Performance Considerations
- **JPA Efficiency**: Avoid N+1 queries, use appropriate fetch strategies
- **Transaction Scope**: Keep transactions short, use read-only when possible
- **H2 Limitations**: Remember this is in-memory database for development

### Security & Validation
- **Input Validation**: Use Jakarta validation in DTOs with meaningful messages
- **SQL Injection**: Spring Data JPA provides protection automatically
- **Error Messages**: Generic user messages, detailed logs for developers