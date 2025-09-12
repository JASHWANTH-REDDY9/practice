# Spring Boot + JWT Auth Backend

This project is a **Spring Boot backend** that provides **JWT-based authentication** (register + login) and is meant to be used with a frontend (React or any client).  

---

## ⚙️ Features
- User **registration** (`/api/auth/register`)  
- User **login** with JWT token (`/api/auth/login`)  
- Passwords stored securely with **BCrypt**  
- CORS enabled for React (`http://localhost:3000`)  
- Custom JWT utility with secure HS512 key  

---

## 📂 Project Structure

### 🔑 Security & Auth
- **`SecurityConfig.java`**  
  Configures Spring Security:  
  - Disables CSRF  
  - Enables CORS for `http://localhost:3000`  
  - Allows `/api/auth/**` endpoints without authentication  
  - Requires JWT for everything else  

- **`JwtUtil.java`**  
  Utility for generating, parsing, and validating JWT tokens using HS512.  
  - ⚠️ Important: `jwt.secret` must be a **64+ character random string** in `application.properties`.

- **`CustomUserDetailsService.java`**  
  Implements Spring Security’s `UserDetailsService`. Loads users from DB by email.  
  - Needed so Spring Security knows how to authenticate users.

---

### 🧑 User Management
- **`User.java`**  
  Entity class mapping users to database (username, email, password).  

- **`UserRepository.java`**  
  Spring Data JPA repository.  
  - `findByEmail(String email)` → to fetch users during login  
  - `existsByEmail(String email)` → to check duplicates during register  

- **`RegisterRequest.java` & `LoginRequest.java`**  
  Payload classes for handling JSON requests from frontend.  

---

### 🎮 Controller
- **`AuthController.java`**  
  Handles `/api/auth/register` and `/api/auth/login`.  
  - On register → saves user with **BCrypt encoded password**  
  - On login → authenticates user, generates JWT, and returns it  

---

## 🐞 Common Errors Faced & Fixes

### 1. ❌ CORS Policy Blocked (Access-Control-Allow-Origin missing)  
**Reason:** Browser blocked request due to missing CORS headers.  
**✅ Fix:** Added CORS configuration in `SecurityConfig`.  

---

### 2. ❌ Invalid credentials even with correct login  
**Reason:** Stored passwords were BCrypt encoded, but login check was not wired with `AuthenticationManager`.  
**✅ Fix:** Created `CustomUserDetailsService` and properly injected `AuthenticationManager`.  

---

### 3. ❌ WeakKeyException: The signing key’s size is not secure enough for HS512  
**Reason:** Default secret key was too short for HS512.  
**✅ Fix:** Generated a 512-bit (64+ char) random key:  

```java
Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
System.out.println(Encoders.BASE64.encode(key.getEncoded()));
```

Add it to `application.properties`:

```properties
jwt.secret=PASTE_YOUR_64+_CHAR_KEY_HERE
jwt.app.jwtExpirationMs=86400000
```

---

### 4. ❌ 403 Forbidden when visiting http://localhost:8080  
**Reason:** Spring Security protects all routes except `/api/auth/**`.  
**✅ Fix:** This is expected — backend is REST API only. Use Thunder Client/Postman/Frontend to test.  

---

### 5. ❌ Request method 'GET' not supported  
**Reason:** You tried opening `/api/auth/register` in the browser (default = GET), but endpoint is POST-only.  
**✅ Fix:** Use POST requests via React or Thunder Client.  

---

## 🚀 How to Run

### Backend (Spring Boot)
```bash
cd backend
mvn spring-boot:run
```

Runs at: [http://localhost:8080](http://localhost:8080)  

---

### Example API Calls

#### Register
```http
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "username": "indra",
  "email": "indra@test.com",
  "password": "pass123"
}
```

#### Login
```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "indra@test.com",
  "password": "pass123"
}
```

**Response:**
```json
{
  "token": "JWT_TOKEN_HERE",
  "username": "indra"
}
```

---

### Frontend (React)
```bash
cd frontend
npm start
```

Runs at: [http://localhost:3000](http://localhost:3000)  

---

## 📝 Notes
- Don’t open `http://localhost:8080` directly in the browser — this is not a UI app, only an API backend.  
- Use Thunder Client/Postman or the React frontend.  
- Always use a long secure secret key for JWT in `application.properties`.  
