# Wallet-Javajadam (Backend)

A high-integrity Digital Wallet System built with **Java 21** and **Spring Boot 3**. 
This project serves as a showcase for implementing financial transaction logic with 
strong emphasis on **Data Consistency**, **Security (JWT/Argon2)**, and **Comprehensive Testing**.

**Version:** v0.0.1

---

## 🏗 Project Structure
- `src/main/java/.../features/` → Feature-based packaging (Wallet, Auth, etc.)
- `src/main/java/.../common/` → Shared DTOs, Exceptions, and Utilities
- `src/main/java/.../controller/` → Handles incoming HTTP requests
- `src/main/java/.../service/` → Contains business logic & transaction management
- `src/main/java/.../repository/` → Manages database access (JPA/Native)
- `src/main/java/.../entity/` → Defines database schemas
- `src/main/resources/` → Application configuration (`application.yml`)

---

## 🛠 Prerequisites & Tech Stack
- **Language:** Java 21 (LTS)
- **Framework:** Spring Boot 3.4
- **Database:** PostgreSQL
- **Caching:** Redis (Session & Performance)
- **MQ:** RabbitMQ (Async Processing)
- **Auth:** Spring Security with JWT & Argon2 hashing
- **Testing:** JUnit 5, Mockito (TDD Approach)
- **Tools:** Lombok, MapStruct, Jackson (JSR310)

---

## 🛡 Security & Design Principles
- **Stateless Auth:** JWT-based authentication via Authorization: Bearer header.
- **Data Integrity:** Transactional consistency for money transfers (ACID).
- **Idempotency:** Designed to prevent double-spending and duplicate transactions.
- **Concurrency:** Handling race conditions in high-traffic wallet operations.

---

## 📥 Getting Started

### 1. Install Dependencies
```bash
./mvnw clean install -DskipTests
```

### 2. Run Project
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### 3. Run UnitT est
ทั้งโปรเจค
```bash
./mvnw test
```

เฉพาะ Service เช่น WalletService
```bash
./mvnw test -Dtest=WalletServiceTest
```

---

## Development Guidelines

### 1. Commit Message Pattern
Follow this conventional commit format:

```
<type>(<scope>): <description>
```

**Types:**
- `feat` - A new feature
- `fix` - A bug fix
- `docs` - Documentation changes
- `style` - Code style changes (formatting, missing semicolons, etc.)
- `refactor` - Code refactoring
- `test` - Adding or updating tests
- `chore` - Maintenance tasks
- `add` - Adding new files or dependencies

**Scope:** The scope should indicate what part of the codebase is affected (e.g., api, docs, readme, auth, database, etc.)

**Examples:**

❌ Bad:
```
feat: auth
done
done: Added JWT token-based authentication for secure login.
```

✅ Good:
```
feat(auth): Added JWT token-based authentication for secure login
fix(database): Fixed connection pool timeout issue
docs(readme): Updated installation instructions
refactor(controller): Simplified user validation logic
test(service): add thoroughness tests for withdrawal
```

---

### 2. Import Paths
Use fully qualified imports or organize packages properly. Avoid wildcard imports (*).
Package structure in pom.xml:

```xml
<groupId>me.pook.wallet</groupId>
<artifactId>wallet-service</artifactId>
```

**Examples:**

❌ Bad:
```java
import com.donttestonprod.*;
import java.util.*;
```

✅ Good:
```java
import com.donttestonprod.model.User;
import com.donttestonprod.repository.UserRepository;
import java.time.LocalDateTime;
```

**Tips:**
```
Use IDE auto-import features (e.g., IntelliJ IDEA) and run mvn formatter:format for consistency.
```

---

### 3. Naming Conventions
#### Follow Java naming conventions:

##### Classes and Interfaces
- **Always PascalCase (UpperCamelCase)**

❌ Bad:
```java
class userProfile {}      // Lowercase
class User_profile {}     // Underscore
```

✅ Good:
```java
public class UserProfile {}
public interface UserRepository {}
```

##### Methods and Variables
- **camelCase (starts with lowercase)**

❌ Bad:
```java
public void IsUserCanAccess() {}  // PascalCase for method
int User_ID;                      // Uppercase with underscore
```

✅ Good:
```java
public void isUserCanAccess() {}
```

---

##### Variables
```java
private String userName;
private boolean isAuthenticated;
```

##### Constants - UPPER_SNAKE_CASE
```java
public static final int MAX_CONNECTIONS = 100;
private static final int DEFAULT_RETRIES = 3;
```

##### Package Names
All lowercase, no underscores, use reverse domain naming

```java
package com.donttestonprod.controller;  // ✅
package com.donttestonprod;            // ✅
package com.donttestonprod.user_service;          // ❌
package com.donttestonprod.userService;           // ❌
```