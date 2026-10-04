# Quora App API

A Quora-style REST backend built with **Spring Boot**. Users register and log in with **JWT authentication**, follow each other, ask questions with topic tags, answer, comment, like, and read a personalised feed of questions from the people they follow.

I built it to learn Spring Security and JWT end to end, so the focus is on getting authentication, authorization and API design right: the logged-in user always comes from the token (never from the request body), ownership is checked on edits, and responses use DTOs so emails and password hashes never leak.

## Tech stack

| Area | Choice |
|---|---|
| Language / build | Java 17, Gradle |
| Framework | Spring Boot 4.1.0 (Web MVC, Data JPA, Security) |
| Database | MySQL |
| Auth | Stateless JWT (jjwt 0.12.6), BCrypt password hashing |
| Other | Lombok, Jakarta Persistence (Hibernate) |

## Features

- **Auth:** register, login, stateless JWT, BCrypt-hashed passwords
- **Profiles:** view anyone's public profile, view and edit your own
- **Follows:** follow other users (no self-follow, no duplicates)
- **Questions:** create with topic tags (topics are created on the fly), paginated search by text and/or tag
- **Answers:** post an answer, edit your own answer
- **Comments:** comment on answers and reply to comments (nested)
- **Likes:** like and unlike questions, answers and comments, one like per user per item
- **Feed:** paginated feed of questions from people you follow, newest first
- **Error handling:** consistent JSON errors with correct status codes

## Getting started

### Prerequisites

- JDK 17
- MySQL 8 running locally
- (Optional) Postman, for the demo collection

### 1. Clone

```bash
git clone https://github.com/arjunpjaiswal/quora-api.git
cd quora-api
```

### 2. Create the database

```sql
CREATE DATABASE quora_db;
```

Tables are created automatically on first start (`spring.jpa.hibernate.ddl-auto=update`). That setting is convenient for development; use proper migrations for production.

The datasource URL and username are set in `src/main/resources/application.properties` (`jdbc:mysql://localhost:3306/quora_db`, user `root`). Change them to match your setup, and prefer a dedicated MySQL user over `root`.

### 3. Set the environment variables

Secrets are **not** stored in the repo. Set these before starting the app:

| Variable | Description |
|---|---|
| `DB_PASSWORD` | Password of the MySQL user in `application.properties` |
| `JWT_SECRET` | **Base64-encoded** signing key that decodes to at least 32 bytes |

`JWT_SECRET` must be Base64. A plain-text secret makes the app fail at startup. Generate one:

```bash
# macOS / Linux / Git Bash
openssl rand -base64 48
```

```powershell
# Windows PowerShell
$b = New-Object byte[] 48
[System.Security.Cryptography.RandomNumberGenerator]::Create().GetBytes($b)
[Convert]::ToBase64String($b)
```

Token lifetime is controlled by `jwt.expiration` in `application.properties` (milliseconds, default `86400000` = 24 hours).

In IntelliJ, add the variables under **Run, Edit Configurations, Environment variables** (`DB_PASSWORD=...;JWT_SECRET=...`). From a terminal:

```bash
export DB_PASSWORD=your_password
export JWT_SECRET=your_base64_secret
```

```powershell
$env:DB_PASSWORD="your_password"
$env:JWT_SECRET="your_base64_secret"
```

### 4. Run

```bash
./gradlew bootRun        # macOS / Linux
.\gradlew bootRun        # Windows
```

The API is then available at `http://localhost:8080`.

## Authentication

1. `POST /api/auth/register` to create an account.
2. `POST /api/auth/login` to receive a token: `{"token":"eyJ..."}`.
3. Send it on every other request:

```
Authorization: Bearer <token>
```

Only `/api/auth/**` is public. Everything else requires a valid token. A missing, expired, tampered or malformed token returns **401**.

## API reference

All request and response bodies are JSON. "Auth" means a Bearer token is required.

### Auth

| Method | Endpoint | Auth | Body | Success |
|---|---|---|---|---|
| POST | `/api/auth/register` | No | `{"userName","email","password"}` (password min 6 chars) | 201, your profile |
| POST | `/api/auth/login` | No | `{"email","password"}` | 200, `{"token"}` |

### Users

| Method | Endpoint | Auth | Notes |
|---|---|---|---|
| GET | `/api/v1/users/me` | Yes | Your own profile, including your email |
| GET | `/api/v1/users/{userId}` | Yes | Public profile (id, userName, bio, createdAt), no email |
| PUT | `/api/v1/users/{userId}` | Yes | Body `{"userName","bio"}`. Only the owner may edit (403 otherwise) |

### Follows

| Method | Endpoint | Auth | Notes |
|---|---|---|---|
| POST | `/api/v1/users/{targetUserId}/follow` | Yes | The follower is you (from the token). 409 if already following, 400 if following yourself |

### Questions and search

| Method | Endpoint | Auth | Notes |
|---|---|---|---|
| POST | `/api/v1/questions` | Yes | Body `{"title","body","topicTags":["Java","Security"]}`. Title is required |
| GET | `/api/v1/questions/search` | Yes | Query: `text`, `tag`, `page` (default 0), `size` (default 10, max 50). All filters optional |

### Answers

| Method | Endpoint | Auth | Notes |
|---|---|---|---|
| POST | `/api/v1/questions/{questionId}/answers` | Yes | Body `{"text"}` |
| PUT | `/api/v1/answers/{answerId}` | Yes | Body `{"text"}`. Only the answer's author may edit (403 otherwise) |

### Comments

| Method | Endpoint | Auth | Notes |
|---|---|---|---|
| POST | `/api/v1/answers/{answerId}/comments` | Yes | Body `{"text"}` |
| POST | `/api/v1/comments/{commentId}/comments` | Yes | Reply to a comment. Body `{"text"}` |

### Likes

`{type}` is one of `questions`, `answers`, `comments`.

| Method | Endpoint | Auth | Notes |
|---|---|---|---|
| POST | `/api/v1/{type}/{id}/likes` | Yes | 201. 409 if you already liked it, 400 for an invalid type |
| DELETE | `/api/v1/{type}/{id}/likes` | Yes | 204. 404 if you have not liked it |

### Feed

| Method | Endpoint | Auth | Notes |
|---|---|---|---|
| GET | `/api/v1/feed` | Yes | Questions by people you follow, newest first. Query: `page` (default 0), `size` (default 10, max 50) |

Search and feed return the same paginated shape:

```json
{
  "items": [
    {
      "id": "…",
      "title": "What is JWT?",
      "body": "Explain JWT simply.",
      "authorId": "…",
      "authorName": "Bob",
      "topics": ["Security", "Java"],
      "createdAt": "2026-10-03T10:56:58.713845Z"
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 1,
  "hasNext": false
}
```

### Errors

Errors always look like this:

```json
{ "status": 409, "message": "User already exists" }
```

| Status | Meaning |
|---|---|
| 400 | Invalid input (missing title, blank text, bad UUID, malformed JSON) |
| 401 | Missing, invalid or expired token, or wrong login credentials |
| 403 | Valid token, but you do not own the resource |
| 404 | Resource does not exist |
| 409 | Conflict (duplicate email, duplicate follow, duplicate like) |
| 500 | Unexpected server error (details are logged, never returned) |

## Try it with Postman

`docs/QuoraApp.postman_collection.json` is a step-by-step demo collection with 62 requests in 8 folders: users and login, follows, questions, answers, feed, comments, likes, and JWT security checks. Each request explains what happens inside the app and has tests that check the response.

1. Start the app against a **fresh database** (registrations expect 201).
2. In Postman, **Import** the collection file.
3. Open the collection and click **Run** to run it top to bottom. Ids and tokens are saved to collection variables automatically, so no copying is needed.

## Security design

- **Stateless JWT.** No server-side sessions. The token carries the user's email as its subject, signed with an HMAC key. Any change to the token breaks the signature.
- **Passwords** are hashed with BCrypt and are never returned by any endpoint.
- **Identity comes from the token.** Controllers read the logged-in user from the security context. No endpoint trusts a user id sent in the request body, so a client cannot post, follow, like or comment as someone else.
- **Authorization.** Editing a profile or an answer requires ownership and returns 403 otherwise.
- **Bad tokens never cause a 500.** The JWT filter catches signature, expiry and malformed-token errors and leaves the request unauthenticated, so Spring Security answers 401.
- **DTO responses.** Entities are not returned directly, so emails and nested objects do not leak. Only your own profile includes your email.
- **Integrity.** Unique constraints on follows and likes back up the duplicate checks in the service layer.
- **Secrets** come from environment variables and are not committed.

## Project structure

```
src/main/java/org/example/quoraappapi
├── controllers   REST endpoints (thin: HTTP in, DTO out)
├── dtos          Request and response shapes
├── exceptions    Custom exceptions and the global error handler
├── models        JPA entities (User, Question, Answer, Comment, Like, Follow, Topic)
├── repositories  Spring Data JPA repositories
├── security      JwtService, JwtAuthFilter, CustomUserDetailsService, SecurityConfig
└── service       Business rules and transactions
```

## Known limitations and roadmap

- No refresh tokens or token revocation: a token stays valid until it expires.
- No roles yet (all authenticated users have the same permissions).
- Email notifications (new answer, new comment, new follower) are planned, not implemented.
- A recommendation engine is planned: rank questions by recency and like count, then personalise by followed users and topics.
- Automated tests are minimal. Planned: MockMvc tests on an in-memory database for register and login, the 403 ownership paths, and feed pagination.
- `ddl-auto=update` and SQL logging are development settings.
