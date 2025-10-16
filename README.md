# spring-boot-starter

This file provides guidance while working with code in this repository.

## Quick Commands

### Setup
- **Build project**: `./gradlew clean build`
- **Run locally**: `./gradlew bootRun`
- **Run with specific profile**: `./gradlew bootRun --args='--spring.profiles.active=local'`
- **Run tests**: `./gradlew test`
- **Run integration tests**: `./gradlew integrationTest`

## Checklist Before Merge
- [ ] Follows Clean Architecture rules
- [ ] DTOs validated with `@Valid` annotations
- [ ] No inline DTO construction in controllers
- [ ] Mapper classes in place and tested
- [ ] Unit & integration tests passing
- [ ] Code formatted