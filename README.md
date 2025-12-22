# Wallet-Javajadam (Backend)

Project For Learning JAVA

**Version:** v0.0.1

---

## 🏗 Project Structure
- `src/main/java/.../controller/` → Handles incoming HTTP requests
- `src/main/java/.../service/` → Contains business logic
- `src/main/java/.../repository/` → Manages database access (JPA/Native)
- `src/main/java/.../entity/` → Defines database schemas
- `src/main/resources/` → Application configuration (`application.yml`)

---

## 🛠 Prerequisites & Tech Stack
- **Java:** JDK 21
- **Framework:** Spring Boot 3.x
- **Database:** PostgreSQL (Docker)
- **Security:** Spring Security with Argon2
- **Tools:** Lombok, MapStruct

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
```

---

### 2. Import Paths
Use fully qualified imports or organize packages properly. Avoid wildcard imports (*).
Package structure in pom.xml:

```xml
<groupId>com.donttestonprod</groupId>
<artifactId>eiei</artifactId>
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