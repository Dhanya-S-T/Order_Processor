# Order Processing Engine


## 1. Testing Strategy

This project strictly follows **Test-Driven Development (TDD)** using the **RED → GREEN → REFACTOR** cycle.

### Approach
- **Tests are written before implementation**
- Each major feature follows:
  - RED: failing unit tests defining expected behavior
  - GREEN: minimal implementation to pass tests
  - Clean commit history reflecting TDD phases

### Types of Tests
- **Unit Tests (JUnit 5)**  
  Validate individual service behavior in isolation.
- **Parameterized Tests**  
  Used for pricing rules to cover multiple discount scenarios.
- **Exception Testing**  
  Ensures invalid flows throw correct custom exceptions.
- **Mockito-based Tests**
  - Dependencies like repositories and services are mocked
  - Verifies interactions (`verify`, `verifyNoInteractions`)

### Tools Used
- JUnit 5
- Mockito
- Maven Surefire Plugin

---

## 2. Code Coverage & Coverage Gaps

### Coverage Tool
- **JaCoCo** is used for code coverage analysis.
- Coverage report is generated automatically during:
```bash
mvn clean test
```

### Report Location
```
target/site/jacoco/index.html
```

### Coverage Summary
- Service layer: High coverage (core business logic fully tested)
- Model layer: Partial coverage (POJOs not explicitly unit-tested)
- Exception classes: Not unit-tested directly

### Coverage Gaps (Intentional)
- **Getters/Setters & POJOs**
  - Not tested as they contain no business logic
- **UnsupportedOperationException methods**
  - Placeholder methods excluded from tests
- **Edge UI / Integration scenarios**
  - Out of scope for unit testing assignment

Overall coverage is sufficient (>70%) and focused on **business-critical logic**.

---

## 3. Design Decisions

### Architecture
- **Layered architecture**
  - Model → Service → Repository
- Clear separation of concerns

### Key Design Choices
- **Service-based design**
  - Pricing, Compliance, and Order logic separated
- **Custom Exceptions**
  - Improves clarity and error handling
- **Dependency Injection**
  - Enables easy mocking and test isolation
- **No framework overuse**
  - Plain Java + Maven for clarity and control

### Why TDD?
- Ensures correctness before implementation
- Improves design quality
- Produces reliable, maintainable code
- Makes refactoring safe

---

## Conclusion
This project demonstrates clean architecture, proper unit testing, and professional TDD practices suitable for academic evaluation and real-world enterprise applications.

