# VISORA — Backend API

VISORA is a web-based design tool for creating visual **backdrops** ("Design stunning backdrops"). Users build multi-page backdrop designs on a canvas, start from published **templates**, submit their own work as templates for review, and keep favorites.

- **Live website (frontend):** https://visora-mauve.vercel.app/
- **Program:** ISTAD Foundation Generation 6

This repository contains the **backend REST API** for VISORA. It does not contain the frontend.

---

## Table of Contents

1. [Project Responsibilities](#project-responsibilities)
2. [Features](#features)
3. [Technology Stack](#technology-stack)
4. [Architecture and Project Structure](#architecture-and-project-structure)
5. [Prerequisites](#prerequisites)
6. [Getting Started](#getting-started)
7. [Environment Configuration](#environment-configuration)
8. [Database Setup](#database-setup)
9. [API Documentation (Swagger)](#api-documentation-swagger)
10. [API Reference](#api-reference)
11. [Frontend Integration Guide](#frontend-integration-guide)
12. [Testing](#testing)
13. [Development Workflow](#development-workflow)
14. [Current Status and Limitations](#current-status-and-limitations)
15. [Contribution Guidelines](#contribution-guidelines)

---

## Project Responsibilities

| Part | Responsibility |
| --- | --- |
| **Backend** (this repository) | Designed and implemented by the project mentor. |
| **Frontend** | Developed by the ISTAD Foundation Generation 6 student team, in a separate codebase. |
| **Integration** | The frontend talks to this backend over HTTP/JSON using the API described below. |

This README is the main technical reference for frontend developers integrating with the API.

---

## Features

All features below are implemented in the current source code.

**Authentication and accounts**
- Register with email, username, phone number, and password (BCrypt-hashed).
- Email verification and password reset via emailed links (Thymeleaf HTML emails over SMTP).
- Login with email and password, returning a JWT access token and refresh token.
- Refresh-token rotation (each refresh revokes the old token), logout, and logout from all devices.
- Role-based access: a user's role name is put into the JWT `scope` claim; admin-only routes require the `ADMIN` role.

**Users and roles**
- User CRUD, current-user profile (`/users/me`), filtered and sorted user search, role assignment.
- Role CRUD.

**Backdrops (the user's designs)**
- Create, read, save, archive, and duplicate backdrops.
- A backdrop is a JSON document: canvas size, orientation, pages, components, groups, animations, transitions, and free-form settings.
- Optimistic locking: saves must send the current `version`; stale saves are rejected.
- Derived metadata computed on the server: `pageCount`, `hasTimer`, `canExport`.
- Search, status filter, timer filter, sorting, and pagination.

**Templates**
- Submit a backdrop as a template for review, or withdraw a pending submission.
- Create, update, archive, and duplicate templates directly.
- Admin review: approve or reject templates (`PENDING` → `APPROVED` / `REJECTED`).
- Public template listing and detail with filters.

**Categories**
- Public category listing with search, active filter, and sorting.
- Admin-only create, update, and delete (soft delete).

**Favorites**
- Favorite exactly one template or one backdrop; list, view, and remove favorites.

**Notifications**
- Admins create notifications for specific users and/or every user with a given role.
- Users list their notifications, get the unread count, and mark one or all as read.

**File storage**
- Multipart file upload to MinIO (S3-compatible), up to 50 MB per file.
- Public file download by generated file name.

---

## Technology Stack

Versions are taken from `gradle/libs.versions.toml` and the Gradle wrapper.

| Area | Technology |
| --- | --- |
| Language | Java 25 (Gradle toolchain) |
| Framework | Spring Boot 4.1.1 (Web MVC, Security, Validation, Data JPA, Mail, Thymeleaf, WebSocket) |
| Build | Gradle 9.3.0 (wrapper), multi-module, version catalog |
| Database | PostgreSQL (JDBC driver managed by Spring Boot) |
| Migrations | Liquibase (SQL changelogs) |
| Security | Spring Security OAuth2 Resource Server (HS256 JWT), JJWT 0.12.6, BCrypt |
| Object storage | MinIO Java SDK 9.0.3 |
| API docs | springdoc-openapi 3.1.0 (Swagger UI) |
| Mapping / boilerplate | MapStruct 1.5.5.Final, Lombok |
| Email | Spring Mail (SMTP) + Thymeleaf templates |
| Container | Multi-stage `Dockerfile` (BellSoft Liberica JDK/JRE 25) |
| CI/CD | GitHub Actions (build multi-arch image → GHCR → deploy over SSH) |

---

## Architecture and Project Structure

The backend uses a **hexagonal (ports and adapters)** style split across Gradle modules:

| Module | Role |
| --- | --- |
| `visora-domain` | Core business logic. Domain entities, use cases, domain exceptions, and **ports** (interfaces) such as `BackdropRepository`, `S3Repository`, `TokenProvider`, `CurrentUser`. Has no web or database code. |
| `visora-application` | Inbound web adapter. REST controllers, request/response DTOs, MapStruct mappers, and `@RestControllerAdvice` exception handlers. |
| `visora-data` | Outbound persistence adapter. JPA entities, Spring Data repositories, adapters implementing the domain repository ports, and Liquibase changelogs. |
| `visora-infrastructure` | Technical adapters and configuration: security, JWT, CORS, MinIO, email, Swagger, WebSocket. |
| `visora-main` | Spring Boot entry point (`VisoraMainApplication`), `application.yaml`, and the only bootable JAR. |

Request flow: **Controller** (`visora-application`) → **Use case** (`visora-domain`) → **Port** → **Adapter** (`visora-data` / `visora-infrastructure`).

```text
visora/
├── build.gradle                    # Shared build config for all modules
├── settings.gradle                 # Module list
├── gradle/libs.versions.toml       # Dependency versions (version catalog)
├── Dockerfile                      # Production image build
├── .github/workflows/workflow.yml  # Build and deploy pipeline (push to main)
├── visora-domain/
│   └── src/main/java/kh/edu/istad/visora/domain/
│       ├── entity/                 # Domain models (User, Backdrop, Template, ...)
│       ├── dto/                    # Domain-level DTOs, filters, Page<T>
│       ├── enums/                  # Sort options, TemplateStatus, ...
│       ├── exception/              # Domain exceptions (mapped to HTTP codes)
│       ├── ports/inbound/usecase/  # Use cases grouped by feature
│       ├── ports/outbound/         # Repository ports
│       └── util/                   # Document metrics, token hashing
├── visora-application/
│   └── src/main/java/kh/edu/istad/visora/application/
│       ├── controller/             # REST controllers
│       ├── dto/                    # Request/response DTOs
│       ├── exception/              # Global exception handlers
│       └── mapper/                 # MapStruct mappers
├── visora-data/
│   └── src/main/
│       ├── java/.../data/          # JPA entities, repositories, adapters
│       └── resources/db/changelog/ # Liquibase master + SQL migrations
├── visora-infrastructure/
│   └── src/main/
│       ├── java/.../infrastructure/ # Security, JWT, MinIO, email, Swagger
│       └── resources/templates/email/ # Verification and reset email templates
└── visora-main/
    └── src/
        ├── main/java/.../main/VisoraMainApplication.java
        ├── main/resources/application.yaml
        └── test/java/.../VisoraMainApplicationTests.java
```

---

## Prerequisites

- **JDK 25** (the Gradle toolchain requires Java 25)
- **PostgreSQL** (any recent version; no version is pinned in the repository)
- **MinIO** or another S3-compatible server reachable by the MinIO SDK
- **SMTP account** for sending emails (the SMTP host is set to `smtp.gmail.com` on port 587 with STARTTLS)
- **Docker** (optional) to run PostgreSQL and MinIO locally or to build the image
- Git

Gradle does not need to be installed; use the included wrapper (`./gradlew`).

---

## Getting Started

### 1. Clone the repository

```bash
git clone <repository-url>
cd visora
```

### 2. Start PostgreSQL and MinIO

The repository does not include a Docker Compose file. The commands below are one convenient way to run both services locally:

```bash
# PostgreSQL on host port 5433
docker run -d --name visora-postgres \
  -e POSTGRES_PASSWORD=<db-password> \
  -p 5433:5432 postgres

# MinIO API on port 9000, console on port 9001
docker run -d --name visora-minio \
  -e MINIO_ROOT_USER=<minio-access-key> \
  -e MINIO_ROOT_PASSWORD=<minio-secret-key> \
  -p 9000:9000 -p 9001:9001 \
  minio/minio server /data --console-address ":9001"
```

The MinIO bucket `visora-bucket` is created automatically on the first upload.

### 3. Configure environment variables

See [Environment Configuration](#environment-configuration) for details.

```bash
export DATABASE_URL=jdbc:postgresql://localhost:5433/postgres
export DATABASE_USER=postgres
export DATABASE_PASSWORD=<db-password>
export MAIL_USERNAME=<smtp-username>
export MAIL_PASSWORD=<smtp-app-password>
export MINIO_ENDPOINT=http://localhost:9000
export MINIO_ACCESS_KEY=<minio-access-key>
export MINIO_SECRET_KEY=<minio-secret-key>
export JWT_SECRET=<random-string-of-at-least-32-characters>
```

### 4. Prepare the database

No manual step is needed. Liquibase runs all migrations automatically when the application starts. See [Database Setup](#database-setup).

### 5. Run the backend

```bash
./gradlew :visora-main:bootRun
```

The API starts on **http://localhost:8080** (Spring Boot default; no custom port is configured).

### 6. Verify it is running

Open Swagger UI in a browser:

```text
http://localhost:8080/swagger-ui.html
```

Or call a public endpoint:

```bash
curl http://localhost:8080/api/v1/categories
```

---

## Environment Configuration

`visora-main/src/main/resources/application.yaml` reads these values through `${...}` placeholders. Set them as environment variables (or as properties in a local, uncommitted Spring profile file).

| Variable | Required | Purpose |
| --- | --- | --- |
| `DATABASE_URL` | Yes | PostgreSQL JDBC URL, e.g. `jdbc:postgresql://localhost:5433/postgres`. Used by JPA and Liquibase. |
| `DATABASE_USER` | Yes | Database username. |
| `DATABASE_PASSWORD` | Yes | Database password. |
| `MAIL_USERNAME` | Yes | SMTP username (sender address). |
| `MAIL_PASSWORD` | Yes | SMTP password or app password. |
| `MINIO_ENDPOINT` | Yes | MinIO/S3 endpoint, e.g. `http://localhost:9000`. |
| `MINIO_ACCESS_KEY` | Yes | MinIO access key. |
| `MINIO_SECRET_KEY` | Yes | MinIO secret key. |
| `JWT_SECRET` | Yes | HMAC-SHA256 signing key for JWTs. Must be at least 32 bytes (256 bits). |
| `SPRING_PROFILES_ACTIVE` | No | Active Spring profile. Defaults to `dev` in `application.yaml`; the Docker image sets `prod`. |

Other settings are fixed in `application.yaml`:

| Setting | Value |
| --- | --- |
| Access token lifetime | `900` seconds (15 minutes) |
| Refresh token lifetime | `604800` seconds (7 days) |
| Max upload size | `50MB` per file and per request |
| MinIO bucket | `visora-bucket` |
| SMTP | `smtp.gmail.com:587`, STARTTLS |
| Email verification link | `https://visora-mauve.vercel.app/auth/verify-email?token=<token>` |
| Password reset link | `https://visora-mauve.vercel.app/auth/reset-password?token=<token>` |

> **Note:** Verification and reset emails always link to the live frontend. To test these flows against a local frontend, change `app.auth.verify-email-url` and `app.auth.reset-password-url` locally (do not commit the change), or copy the `token` from the link and call the API directly.

> **Security:** Never commit real credentials. Keep secrets in environment variables or in a local profile file that is excluded from Git.

---

## Database Setup

- **Database:** PostgreSQL. Several columns use `jsonb` (backdrop/template pages, canvas, settings; notification content), so PostgreSQL is required.
- **Schema management:** Liquibase runs automatically at startup using `visora-data/src/main/resources/db/changelog/db.changelog-master.yaml`, which includes every file in `db/changelog/ddl/` in file-name order (`0001-...sql` to `0018-...sql`).
- **Hibernate DDL is disabled** (`ddl-auto: none`). All schema changes must be made as new Liquibase changelog files.
- **Soft delete:** most tables have an `is_deleted` column. Several delete operations set a flag instead of removing rows.
- **Auditing:** `created_at`, `created_by`, `updated_at`, `updated_by` are filled by JPA auditing.

### Main tables

| Table | Purpose |
| --- | --- |
| `users`, `roles` | Accounts and roles (each user has at most one role). |
| `refresh_tokens`, `email_verification_tokens`, `password_reset_tokens` | Authentication tokens (verification and reset tokens are stored as SHA-256 hashes). |
| `categories` | Template categories. |
| `backdrops` | User designs (document stored as `jsonb`). |
| `templates` | Templates, including review status and category. |
| `favorites` | Favorites pointing to exactly one template or one backdrop. |
| `notifications`, `notification_recipients` | Notifications and per-user read state. |
| `storages` | Metadata for uploaded files (the files themselves are in MinIO). |

### Seed data

**No seed data is included.** In particular, no roles exist after the first start. To use admin-only endpoints you must:

1. Create a role named exactly `ADMIN` (`POST /api/v1/roles`).
2. Assign it to a user (`PATCH /api/v1/users/{uuid}/role`).
3. Log in again so the new access token contains `"scope": ["ADMIN"]`.

---

## API Documentation (Swagger)

springdoc-openapi is enabled.

| Resource | Local URL |
| --- | --- |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| OpenAPI JSON | http://localhost:8080/v3/api-docs |

Swagger UI has an **Authorize** button ("Access Token" bearer scheme). Paste the `accessToken` from the login response to call protected endpoints.

The OpenAPI config (`SwaggerConfig.java`) lists two servers: `https://visora-api.gital.me` and `http://localhost:8080`. Confirm with the mentor which hosted API URL is currently in use before relying on it.

Swagger is the most complete source for every field. The reference below covers what frontend developers need most often.

---

## API Reference

**Base path:** `/api/v1` (except the public file download at `/storage/{fileName}`)

**Auth column:**
- **Public:** no token needed.
- **User:** requires `Authorization: Bearer <accessToken>`.
- **Admin:** requires a token whose user has the `ADMIN` role.

### Response envelope

Every successful JSON response uses this wrapper:

```json
{
  "data": { },
  "status": 200,
  "success": true,
  "message": "Login successful"
}
```

For operations with no result (delete, logout, etc.), `data` is an empty string `""`.

### Pagination format

List endpoints return a `Page` inside `data`. Page numbers are **zero-based**, and the default page size is `25`.

```json
{
  "data": {
    "contents": [],
    "pageNumber": 0,
    "pageSize": 25,
    "totalElements": 0,
    "totalPages": 0
  },
  "status": 200,
  "success": true,
  "message": "Retrieved categories data"
}
```

### Error format

```json
{
  "message": "Not Found",
  "detail": "Backdrop was not found",
  "status": 404
}
```

Validation errors (`@Valid` request bodies) return `400` with a list in `detail`:

```json
{
  "message": "Bad Request",
  "detail": [
    { "field": "email", "reason": "Invalid email format" },
    { "field": "password", "reason": "Password must be at least 8 characters" }
  ],
  "status": 400
}
```

Requests rejected by Spring Security (missing or invalid token → `401`, missing admin scope → `403`) return an **empty body** instead of this JSON format.

---

### Authentication — `/api/v1/auth`

| Method | Path | Auth | Purpose |
| --- | --- | --- | --- |
| POST | `/auth/register` | Public | Create an account and send a verification email. |
| POST | `/auth/login` | Public | Log in with email and password. |
| POST | `/auth/refresh` | Public | Exchange a refresh token for new tokens (rotation). |
| POST | `/auth/logout` | Public | Revoke a refresh token. |
| POST | `/auth/logout-all` | User | Revoke all refresh tokens of the current user. |
| POST | `/auth/verify-email` | Public | Verify an email using the token from the email link. |
| POST | `/auth/resend-verification` | Public | Send a new verification email. |
| POST | `/auth/forgot-password` | Public | Send a password reset email. |
| POST | `/auth/reset-password` | Public | Set a new password using the reset token. |

**Register** — `POST /api/v1/auth/register` → `201`

```json
{
  "firstName": "Sok",
  "lastName": "Dara",
  "username": "sokdara",
  "email": "sokdara@example.com",
  "phoneNumber": "012345678",
  "password": "Password123",
  "confirmPassword": "Password123"
}
```

Validation: `firstName`/`lastName` at least 2 characters, `username` at least 5, valid `email`, `password` at least 8, all fields required. Duplicate username, email, or phone number returns `409`. Mismatched passwords return `400`.

```json
{
  "data": { "userUuid": "8f0c7a7e-1d2b-4c1e-9a55-1f6c2b3d4e5f" },
  "status": 201,
  "success": true,
  "message": "Registration successful"
}
```

> The request uses `firstName`/`lastName`. User responses use `givenName`/`familyName`.

**Login** — `POST /api/v1/auth/login` → `200`

```json
{ "email": "sokdara@example.com", "password": "Password123" }
```

```json
{
  "data": {
    "tokenType": "Bearer",
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
    "expiresIn": 900
  },
  "status": 200,
  "success": true,
  "message": "Login successful"
}
```

A wrong password returns `401` (`"Invalid email or password"`). An unknown email returns `404` (`"User not found"`).

**Refresh** — `POST /api/v1/auth/refresh` with `{ "refreshToken": "..." }` → `200` and the same shape as login. The old refresh token is revoked. Always store the **new** refresh token. Invalid, revoked, or expired tokens return `401`.

**Logout** — `POST /api/v1/auth/logout` with `{ "refreshToken": "..." }` → `200`.

**Verify email** — `POST /api/v1/auth/verify-email` with `{ "token": "<token from link>" }` → `200`.
Tokens expire after 30 minutes and can be used only once (`401` otherwise).

**Resend verification / Forgot password** — body `{ "email": "sokdara@example.com" }` → `200`.

**Reset password** — `POST /api/v1/auth/reset-password` → `200`. A successful reset also revokes all of the user's refresh tokens.

```json
{ "token": "<token from link>", "newPassword": "NewPassword123", "confirmPassword": "NewPassword123" }
```

---

### Users — `/api/v1/users`

| Method | Path | Auth | Purpose |
| --- | --- | --- | --- |
| GET | `/users/me` | User | Current user's profile. |
| GET | `/users?pageNumber=0&pageSize=25` | User | Paginated users. |
| POST | `/users/filter` | User | Filtered users (JSON body, see below). |
| GET | `/users/{uuid}` | User | User by UUID. |
| POST | `/users` | User | Create a user (no password field). |
| PUT | `/users/{uuid}` | User | Update a user. |
| DELETE | `/users/{uuid}` | User | Soft-delete a user. |
| PATCH | `/users/{uuid}/role` | User | Assign a role: `{ "roleUuid": "..." }`. |
| DELETE | `/users/{uuid}/role` | User | Remove the user's role. |

> Only authentication is enforced on these routes, not the `ADMIN` role. The frontend should show user and role management only in admin screens.

**Filter body** (all fields optional): `search` matches username, names, email, and phone number; `role` filters by role name; `sort` is one of `USERNAME_ASC`, `USERNAME_DESC`, `NEWEST` (default), `OLDEST`.

```json
{ "pageNumber": 0, "pageSize": 25, "search": "dara", "role": "ADMIN", "sort": "NEWEST" }
```

**User object**

```json
{
  "uuid": "8f0c7a7e-1d2b-4c1e-9a55-1f6c2b3d4e5f",
  "username": "sokdara",
  "familyName": "Dara",
  "givenName": "Sok",
  "gender": null,
  "email": "sokdara@example.com",
  "emailVerified": true,
  "phoneNumber": "012345678",
  "picture": null,
  "role": { "uuid": "b1d2...", "role": "ADMIN" },
  "isDeleted": false,
  "createdAt": "...",
  "updatedAt": "...",
  "createdBy": "...",
  "updatedBy": "..."
}
```

**Update body** (`PUT /users/{uuid}`): `username`, `familyName`, `givenName`, `gender` (at least 4 characters), `email`, `emailVerified`, `phoneNumber`, `picture`.

---

### Roles — `/api/v1/roles`

| Method | Path | Auth | Purpose |
| --- | --- | --- | --- |
| GET | `/roles?pageNumber=0&pageSize=25` | User | Paginated roles. |
| GET | `/roles/{uuid}` | User | Role by UUID. |
| POST | `/roles` | User | Create: `{ "role": "ADMIN", "description": "..." }` (`role` is at least 3 characters). |
| PUT | `/roles/{uuid}` | User | Update (same body). |
| DELETE | `/roles/{uuid}` | User | Delete. |

---

### Categories — `/api/v1/categories`

| Method | Path | Auth | Purpose |
| --- | --- | --- | --- |
| GET | `/categories` | Public | Paginated list. |
| GET | `/categories/{uuid}` | Public | Category by UUID. |
| POST | `/categories` | Admin | Create. |
| PATCH | `/categories/{uuid}` | Admin | Update (send the full object). |
| DELETE | `/categories/{uuid}` | Admin | Soft delete. |

Query parameters for `GET /categories`: `pageNumber`, `pageSize`, `search` (name/slug/description), `isActive` (`true`/`false`), `sort` (`DISPLAY_ORDER` default, `NAME_ASC`, `NAME_DESC`, `NEWEST`, `OLDEST`; unknown values fall back to the default).

Request body (create/update):

```json
{
  "name": "Wedding",
  "slug": "wedding",
  "description": "Wedding backdrops",
  "icon": "heart",
  "displayOrder": 1,
  "isActive": true
}
```

Duplicate `name` or `slug` returns `409`. Response object adds `uuid`, `templateCount`, and audit fields.

---

### Backdrops — `/api/v1/backdrops`

All backdrop endpoints require **User** auth. A user can only access their own backdrops (`403` otherwise).

| Method | Path | Purpose |
| --- | --- | --- |
| GET | `/backdrops` | Current user's backdrops (summaries). |
| POST | `/backdrops` | Create a backdrop → `201`, returns a summary. |
| GET | `/backdrops/{backdropUuid}` | Full backdrop document (also updates `lastOpenedAt`). |
| PATCH | `/backdrops/{backdropUuid}` | Save changes (optimistic locking). |
| DELETE | `/backdrops/{backdropUuid}` | Archive (sets `status` to `ARCHIVED`). |
| POST | `/backdrops/{backdropUuid}/duplicate` | Duplicate. Optional body `{ "name": "..." }` (default: `"Copy of <name>"`). |
| POST | `/backdrops/{backdropUuid}/templates` | Submit the backdrop as a template for review. |
| DELETE | `/backdrops/{backdropUuid}/templates` | Withdraw the pending template submission. |

Query parameters for `GET /backdrops`: `search` (name), `status` (`ACTIVE`/`ARCHIVED`), `hasTimer` (`true`/`false`), `sort` (`LAST_UPDATED` default, `NEWEST`, `OLDEST`, `NAME_ASC`, `NAME_DESC`), `pageNumber`, `pageSize`.

**Create** — required: `name`, `orientation` (`LANDSCAPE`/`PORTRAIT`), `canvas`, `pages`. Each page needs `uuid` and `pageNumber` (at least 1). Each component needs `uuid` and `type`. The `vector`, `background`, `animation`, `styles`, `timer`, and `settings` fields accept free-form JSON that the backend stores unchanged.

```json
{
  "clientSchemaVersion": 1,
  "name": "Annual Party Stage",
  "thumbnail": null,
  "orientation": "LANDSCAPE",
  "canvas": { "width": 1920, "height": 1080 },
  "settings": {},
  "pages": [
    {
      "uuid": "page-1",
      "name": "Page 1",
      "pageNumber": 1,
      "background": { "color": "#000000" },
      "groups": [],
      "animations": [],
      "transition": { "preset": "fade", "durationMs": 500, "delayMs": 0 },
      "components": [
        {
          "uuid": "cmp-1",
          "type": "text",
          "content": "Welcome",
          "position": { "x": 100, "y": 200 },
          "size": { "width": 600, "height": 120 },
          "rotation": 0,
          "layerIndex": 1,
          "locked": false,
          "visible": true,
          "styles": { "fontSize": 64, "color": "#ffffff" }
        }
      ]
    }
  ]
}
```

**Backdrop summary** (list, create, save, and duplicate responses):

```json
{
  "uuid": "3c9e...",
  "ownerUuid": "8f0c...",
  "sourceTemplateUuid": null,
  "name": "Annual Party Stage",
  "thumbnail": null,
  "orientation": "LANDSCAPE",
  "pageCount": 1,
  "hasTimer": false,
  "canExport": true,
  "status": "ACTIVE",
  "version": 1,
  "lastOpenedAt": "...",
  "lastSavedAt": "...",
  "createdAt": "...",
  "updatedAt": "..."
}
```

`GET /backdrops/{uuid}` returns the same fields plus `canvas`, `pages`, and `settings`.

**Save** — `PATCH /backdrops/{uuid}`. `version` is **required** and must equal the current version. Omitted (`null`) fields keep their existing values. On success, `version` increases by 1. A stale version returns `409`. Reload the backdrop before saving again.

```json
{ "version": 1, "name": "Annual Party Stage v2", "pages": [ ... ] }
```

**Submit as template** — `POST /backdrops/{uuid}/templates` → `201` with a template object (`templateStatus: "PENDING"`). Only one pending submission per backdrop is allowed (`409` otherwise). All fields are optional; `proposedName` defaults to the backdrop name.

```json
{
  "proposedName": "Party Stage",
  "description": "Dark stage backdrop",
  "categoryUuid": "<category uuid>",
  "eventType": "party",
  "styles": ["modern"],
  "colors": ["#000000", "#ffffff"]
}
```

---

### Templates — `/api/v1/templates`

| Method | Path | Auth | Purpose |
| --- | --- | --- | --- |
| GET | `/templates` | Public | All templates (summaries). |
| GET | `/templates/{templateUuid}` | Public | Full template document. |
| GET | `/templates/me` | User | Current user's templates. |
| POST | `/templates` | User | Create a template directly (starts as `PENDING`). |
| PATCH | `/templates/{templateUuid}` | User (owner) | Save changes (optimistic locking with `version`). |
| DELETE | `/templates/{templateUuid}` | User | Archive. |
| POST | `/templates/{templateUuid}/duplicate` | User (owner) | Duplicate (optional `{ "name": "..." }`). |
| POST | `/templates/{templateUuid}/approve` | Admin | Set `templateStatus` to `APPROVED`. |
| POST | `/templates/{templateUuid}/reject` | Admin | Set `templateStatus` to `REJECTED`. |

Query parameters for both list endpoints: `search`, `status` (`ACTIVE`/`ARCHIVED`), `templateStatus` (`PENDING`/`APPROVED`/`REJECTED`), `hasTimer`, `sort` (same values as backdrops), `pageNumber`, `pageSize`.

> `GET /templates` returns templates of **every** review status unless you filter. For a public gallery, call `GET /api/v1/templates?templateStatus=APPROVED&status=ACTIVE`.

**Create / update body:** same document fields as backdrops, plus `description`, `categoryUuid`, `eventType`, `styles` (string array), and `colors` (string array). Update also requires `version`.

**Template summary** fields: `uuid`, `ownerUuid`, `sourceBackdropUuid`, `sourceTemplateUuid`, `name`, `description`, `thumbnail`, `orientation`, `pageCount`, `hasTimer`, `canExport`, `status`, `templateStatus`, `submittedBy`, `categoryUuid`, `eventType`, `submittedAt`, `version`, `lastOpenedAt`, `lastSavedAt`, `createdAt`, `updatedAt`.
The full template response adds `canvas`, `pages`, `settings`, `proposedName`, `styles`, and `colors`.

---

### Favorites — `/api/v1/favorites`

All favorite endpoints require **User** auth and only work on the current user's favorites.

| Method | Path | Purpose |
| --- | --- | --- |
| GET | `/favorites` | List. Query: `pageNumber`, `pageSize`, `type` (`TEMPLATE`/`BACKDROP`), `sort` (`NEWEST` default, `OLDEST`). |
| GET | `/favorites/{favoriteUuid}` | Get one. |
| POST | `/favorites` | Add → `201`. |
| DELETE | `/favorites/{favoriteUuid}` | Remove. |

Send **exactly one** of `templateUuid` or `backdropUuid`. Sending both or neither is rejected. Adding something that is already a favorite returns `409`.

```json
{ "templateUuid": "<template uuid>" }
```

```json
{
  "data": {
    "uuid": "f1a2...",
    "ownerUuid": "8f0c...",
    "type": "TEMPLATE",
    "targetUuid": "<template uuid>",
    "createdAt": "..."
  },
  "status": 201,
  "success": true,
  "message": "Favorite added successfully"
}
```

---

### Notifications — `/api/v1/notifications`

| Method | Path | Auth | Purpose |
| --- | --- | --- | --- |
| GET | `/notifications` | User | Current user's notifications. Query: `pageNumber`, `pageSize`, `type`, `read` (`true`/`false`), `sort` (`NEWEST`/`OLDEST`). |
| GET | `/notifications/unread-count` | User | `data` is a number. |
| PATCH | `/notifications/{notificationUuid}/read` | User | Mark one as read. |
| PATCH | `/notifications/read-all` | User | Mark all as read; `data` is the number updated. |
| POST | `/notifications` | Admin | Create a notification. |

`type` is one of `TEMPLATE_SUBMITTED`, `TEMPLATE_APPROVED`, `TEMPLATE_REJECTED`. `content` is any JSON value. Provide `recipientUuids`, `recipientRole`, or both.

```json
{
  "type": "TEMPLATE_APPROVED",
  "content": { "templateUuid": "<uuid>", "message": "Your template was approved" },
  "recipientUuids": ["<user uuid>"],
  "recipientRole": null
}
```

Notification item (as returned to a recipient):

```json
{
  "notificationUuid": "a7c1...",
  "type": "TEMPLATE_APPROVED",
  "content": { "templateUuid": "<uuid>", "message": "Your template was approved" },
  "read": false,
  "readAt": null,
  "createdAt": "..."
}
```

---

### File storage

| Method | Path | Auth | Purpose |
| --- | --- | --- | --- |
| POST | `/api/v1/storage` | User | Upload one file (`multipart/form-data`, field name `file`, max 50 MB) → `201`. |
| GET | `/api/v1/storages` | User | Paginated list of all uploaded files. |
| GET | `/api/v1/storages/{userUuid}` | User | Paginated files uploaded by a user. |
| GET | `/storage/{fileName}` | Public | Download/display the file (served inline with its MIME type). |

Note the different paths: `/api/v1/storage` (upload), `/api/v1/storages` (list), and `/storage/{fileName}` (public file, no `/api/v1` prefix).

Upload response:

```json
{
  "data": {
    "uuid": "0d4e...",
    "originalFileName": "logo.png",
    "fileName": "9b1f0c2e6a7d4c3b8e5f1a2b3c4d5e6f.png",
    "extension": "png",
    "mimeType": "image/png",
    "createdBy": "...",
    "createdAt": "...",
    "updatedBy": "...",
    "updatedAt": "..."
  },
  "status": 201,
  "success": true,
  "message": "File uploaded successfully"
}
```

The public URL of the uploaded file is `<API_BASE_URL>/storage/<fileName>`. Store this URL (or the `fileName`) in fields such as `thumbnail`, component `image`, or user `picture`.

---

## Frontend Integration Guide

### Base URLs

| What | URL |
| --- | --- |
| Live frontend | https://visora-mauve.vercel.app/ |
| Local backend | `http://localhost:8080` |
| Hosted backend | Not documented here. Ask the mentor; the API is **not** served from the Vercel domain. |

Keep the API URL in an environment variable, not in code. For example, in a Vite project:

```bash
# .env.local (frontend project)
VITE_API_BASE_URL=http://localhost:8080
```

```js
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;
```

### CORS

CORS is open to all origins (`allowedOriginPatterns: *`), with methods `GET, POST, PUT, PATCH, DELETE, OPTIONS`, all headers, and credentials allowed. A frontend on `http://localhost:5173` (or any other port) can call a local backend without extra configuration.

### Authentication flow

1. `POST /api/v1/auth/login` → store `accessToken` and `refreshToken`.
2. Send `Authorization: Bearer <accessToken>` on every protected request.
3. The access token expires after **15 minutes** (`expiresIn: 900`). On a `401`, call `POST /api/v1/auth/refresh` once, then **replace both tokens** and retry the original request. Refresh tokens rotate; an old one stops working after it is used.
4. If refresh fails, clear the tokens and send the user to the login page.
5. On logout, call `POST /api/v1/auth/logout` with the refresh token, then clear local tokens.

Tokens are returned in the JSON body. The backend does not set cookies. To find the user's role, call `GET /api/v1/users/me` and read `data.role.role`, or decode the access token's `scope` claim.

### Email links

Verification and reset emails link to the frontend routes:

- `/auth/verify-email?token=...` → frontend reads `token` and calls `POST /api/v1/auth/verify-email`.
- `/auth/reset-password?token=...` → frontend shows a form and calls `POST /api/v1/auth/reset-password`.

### Example client (fetch)

```js
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

async function api(path, { method = "GET", body, token } = {}) {
  const headers = {};
  if (body !== undefined) headers["Content-Type"] = "application/json";
  if (token) headers["Authorization"] = `Bearer ${token}`;

  const res = await fetch(`${API_BASE_URL}${path}`, {
    method,
    headers,
    body: body !== undefined ? JSON.stringify(body) : undefined,
  });

  // 401/403 from Spring Security have an empty body
  const text = await res.text();
  const json = text ? JSON.parse(text) : null;

  if (!res.ok) {
    // json is { message, detail, status } or null
    throw { status: res.status, error: json };
  }
  return json.data; // unwrap the ApiResponse envelope
}

// Login
const tokens = await api("/api/v1/auth/login", {
  method: "POST",
  body: { email: "sokdara@example.com", password: "Password123" },
});

// Public approved template gallery
const page = await api("/api/v1/templates?templateStatus=APPROVED&status=ACTIVE&pageNumber=0&pageSize=12");
console.log(page.contents, page.totalPages);

// Current user's backdrops
const myBackdrops = await api("/api/v1/backdrops?sort=LAST_UPDATED", {
  token: tokens.accessToken,
});
```

### File upload example

Do **not** set `Content-Type` yourself. The browser adds the multipart boundary.

```js
const form = new FormData();
form.append("file", fileInput.files[0]);

const res = await fetch(`${API_BASE_URL}/api/v1/storage`, {
  method: "POST",
  headers: { Authorization: `Bearer ${accessToken}` },
  body: form,
});
const { data } = await res.json();
const imageUrl = `${API_BASE_URL}/storage/${data.fileName}`;
```

### Saving the editor (optimistic locking)

1. Load the backdrop with `GET /api/v1/backdrops/{uuid}` and keep `version`.
2. Save with `PATCH /api/v1/backdrops/{uuid}` and include that `version`.
3. Replace the stored `version` with the one in the response (`version + 1`).
4. On `409`, tell the user the backdrop changed elsewhere and reload it.

Templates work the same way with `PATCH /api/v1/templates/{uuid}`.

### HTTP status codes

| Code | Meaning in this API |
| --- | --- |
| `200` | Success. |
| `201` | Resource created (register, create, duplicate, upload, submit, favorite). |
| `400` | Validation failed, malformed JSON, or passwords do not match. |
| `401` | Missing/invalid/expired access token (empty body), or auth failure such as a wrong password or invalid email/reset token (JSON body). |
| `403` | Not the owner of the resource, or missing the `ADMIN` role. |
| `404` | Resource not found. |
| `409` | Duplicate (username, email, phone, category, role, favorite), version conflict, or a template is already pending. |
| `500` | Unhandled server error. Some domain validation errors without a specific handler also return this code. Read `detail` for the message. |

### Conventions

- Identifiers in paths are UUID strings (`uuid` fields), not numeric IDs.
- Enum values are uppercase strings (`LANDSCAPE`, `ACTIVE`, `APPROVED`, ...).
- Sort parameters are case-insensitive. Unknown sort values fall back to the endpoint's default.
- Deletes of backdrops and templates **archive** them (`status: "ARCHIVED"`). Filter by `status=ACTIVE` to hide archived items.
- Timestamps are serialized by Jackson from `java.sql.Timestamp`; check a real response or Swagger for the exact format.

---

## Testing

The project uses JUnit 5 with Spring Boot Test (`spring-boot-starter-test`).

```bash
./gradlew test
```

The current suite is `visora-main/src/test/java/kh/edu/istad/visora/main/VisoraMainApplicationTests.java`:

- `contextLoads`: starts the full application context.
- `sendTestEmail`: **sends a real email** through the configured SMTP account.

Both tests need a working database, MinIO configuration, SMTP credentials, and all environment variables. No unit tests or mocked integration tests exist yet. Test results are not documented here; run the suite in your own environment.

To build without running tests:

```bash
./gradlew build -x test
```

---

## Development Workflow

| Task | Command |
| --- | --- |
| Run locally | `./gradlew :visora-main:bootRun` |
| Compile all modules | `./gradlew build -x test` |
| Run tests | `./gradlew test` |
| Build the runnable JAR | `./gradlew :visora-main:bootJar` → `visora-main/build/libs/visora-main.jar` |
| Run the JAR | `java -jar visora-main/build/libs/visora-main.jar` |
| Build the Docker image | `docker build -t visora-api .` |
| Run the Docker image | `docker run -p 8080:8080 --env-file <your-env-file> visora-api` |

**CI/CD:** every push to `main` runs `.github/workflows/workflow.yml`. The workflow builds a multi-architecture image (`linux/amd64`, `linux/arm64`), pushes it to GitHub Container Registry, and redeploys the `visora-app` service on the server with Docker Compose. Pushing to `main` deploys the change.

No linter or formatter is configured in the build.

---

## Current Status and Limitations

Frontend developers should know about the following before integrating:

- **No seed data.** There are no roles or admin users until you create them (see [Seed data](#seed-data)).
- **Real-time notifications are not active.** A STOMP WebSocket endpoint (`/ws`) is configured, but no code sends messages to it. Poll `GET /api/v1/notifications/unread-count` instead.
- **Notifications are not created automatically** when templates are submitted, approved, or rejected. They are created only through `POST /api/v1/notifications` (admin).
- **Email verification is not enforced at login.** Unverified users can still log in.
- **`POST /api/v1/auth/logout-all` may not work** with JWT bearer authentication. It reads the principal as a domain `User`, but the JWT principal is a different type. Verify it before you rely on it.
- **`GET /api/v1/templates` includes pending and rejected templates** unless you filter by `templateStatus`.
- **No export endpoint.** `canExport` is only a computed flag; the API does not render or export backdrops.
- **Admin role checks** are enforced only for category writes, template approve/reject, and notification creation. User and role management routes require authentication only.
- **Email links point to the production frontend**, even when the backend runs locally.

---

## Contribution Guidelines

- **Branches:** create a feature branch from `main` (for example `feature/backdrop-export` or `fix/logout-all`). Open a pull request instead of pushing to `main`, because a push to `main` deploys to production.
- **Commits:** keep commits small and focused. Use clear messages that describe the change (for example `Add category filter to template list`).
- **Architecture:** follow the existing module boundaries:
  - Business rules go in `visora-domain` use cases.
  - HTTP concerns (controllers, DTOs, validation, exception mapping) go in `visora-application`.
  - JPA code and migrations go in `visora-data`.
  - Technical integrations go in `visora-infrastructure`.
- **Database changes:** add a new numbered SQL file in `visora-data/src/main/resources/db/changelog/ddl/`. Never edit a migration that has already been applied.
- **API contract changes:** the frontend team depends on these endpoints. When you change a path, field name, enum value, or response shape:
  - Update this README and check Swagger.
  - Tell the frontend team before merging.
  - Prefer adding fields over renaming or removing them.
- **Secrets:** never commit credentials, tokens, or personal email addresses. Use environment variables.
- **Testing:** run `./gradlew build` before opening a pull request, and add tests for new business logic where practical.
