# WorkBridge — Full-Stack Freelance & Job Marketplace

A production-style **Upwork/Fiverr-style marketplace platform** built end-to-end as a solo full-stack project: a Spring Boot REST API, an Angular SPA, and a native Android client, all backed by a single normalized MySQL schema. The platform supports two parallel marketplace models — **fixed-price job postings** (traditional hiring) and **gig-based freelance services** (productized offerings) — plus in-app payments, messaging, notifications, and AI-assisted hiring tools.

> **Repo layout:** this is a monorepo containing three independently deployable applications that share one backend contract.
> - `SpringBoot_Backend/project` — REST API (Java / Spring Boot)
> - `Angular_Frontend/WorkBridgeAngular` — web client (Angular)
> - `Android_Frontend_Using_Java/WorkBridgeAndroid` — native mobile client (Java)
> - `MySQL_Database_Data/workbridge.sql` — full schema + seed data (50 tables)

---

## Why this project stands out

- **Real multi-role marketplace domain**, not a CRUD tutorial clone: freelancers, companies, and admins each get distinct dashboards, permissions, and workflows across a 50-table relational schema covering jobs, gigs, applications, orders, contracts, payments, wallets, reviews, and disputes.
- **Three clients, one contract.** The same Spring Boot API serves an Angular web app and a native Android app, proving the backend was designed as a real API product, not glued to a single frontend.
- **Security done properly, not bolted on.** Stateless JWT authentication + Spring Security method-level `@PreAuthorize` authorization enforced consistently across 40+ REST controllers, with ownership-based access checks (a freelancer can only touch their own resume, a company can only manage its own job postings, etc.).
- **Real payment integration**, not a mock checkout — SSLCommerz gateway integration with signed callback verification, wallet crediting, and transaction ledgering.
- **AI-assisted hiring features** — Google Gemini integration for resume parsing/autofill and AI-generated interview questions, going beyond typical portfolio-project scope.
- **Scheduled background jobs** (Spring `@Scheduled`) for real marketplace mechanics like automatic dispute-window expiry on gig orders.

---

## Tech Stack

| Layer | Technology |
|---|---|
| **Backend** | Java, Spring Boot 4, Spring Web MVC, Spring Data JPA / Hibernate, Spring Security, MySQL |
| **Auth** | JJWT (JWT issuing/validation), BCrypt password hashing, stateless sessions |
| **Backend integrations** | Google Gemini API (AI resume parsing & interview questions), SSLCommerz (payment gateway), Jakarta Mail (email/OTP), Apache PDFBox + OpenHTMLtoPDF (resume/PDF generation), Apache POI (document processing), springdoc-openapi (Swagger/OpenAPI docs) |
| **Frontend (Web)** | Angular 21 (standalone components), TypeScript, RxJS, Bootstrap 5, SweetAlert2, JWT decode |
| **Mobile** | Native Android (Java), Retrofit + OkHttp (networking), Gson, Glide (image loading), Lombok |
| **Database** | MySQL — 50 tables, normalized schema, join tables for many-to-many domains (skills, categories, geo hierarchy) |
| **Build tools** | Maven (backend), Angular CLI / npm (web), Gradle (Android) |

---

## Architecture

```
                ┌─────────────────────┐
                │   MySQL Database     │
                │  (50 tables, JPA)    │
                └──────────▲───────────┘
                           │
                ┌──────────┴───────────┐
                │   Spring Boot API     │
                │  JWT Auth · @PreAuthorize
                │  Gemini AI · SSLCommerz│
                └───▲──────────────▲────┘
                    │              │
        ┌───────────┴───┐   ┌──────┴─────────┐
        │ Angular Web SPA│   │ Android (Java) │
        │  (Bootstrap 5) │   │ Retrofit/OkHttp│
        └────────────────┘   └────────────────┘
```

All three surfaces speak to the same REST contract; CORS and JWT are configured to serve `localhost:4200` (Angular dev server) and `10.0.2.2` (Android emulator) simultaneously.

---

## 1. Backend — `SpringBoot_Backend/project`

**~390 Java files** organized by domain module (not generic layers), e.g. `gig/`, `gigorder/`, `jobapplication/`, `payment/`, `wallet/`, `withdraw/`, `conversation/`, `notification/`, `review/`, `report/`, `ai/`, `sslcommerz/`, `security/` — each with its own controller, service/serviceImpl, repository, entity, mapper, and request/response DTOs.

### Core business features implemented
- **Dual marketplace model** — traditional `Job` postings with `JobApplication` workflows *and* Fiverr-style `Gig` listings with `GigOrder` lifecycle (placed → in-progress → delivered → disputed/completed).
- **Company & freelancer profiles** — full profile system (`Education`, `Experience`, `Portfolio`, `Training`, `Reference`, `Extracurricular`, `UserSkill`, `UserLanguage`) plus `CompanyProfile` with public company pages.
- **Geo-hierarchical address system** — `Country → Division → District → PoliceStation → Address`, modeled as a real relational hierarchy rather than flat text fields.
- **In-app wallet & payments** — `Wallet`, `Transaction`, `Payment`, and `Withdraw` entities backing a real money flow: SSLCommerz-initiated deposits, wallet crediting on payment success, and a separate withdrawal request/approval pipeline.
- **Messaging & notifications** — `Conversation`/`Message` real-time-style chat between clients and freelancers, plus a `Notification` system with type-based routing (new message, application status change, payment events, etc.).
- **Reviews & trust/safety** — `Review` system for completed orders/jobs, plus a `Report` moderation pipeline with `ReportStatus`/`ReportType` for admin review.
- **Admin operations** — dedicated `AdminDashboardDTO`, user search via JPA `Specification` (dynamic filtering), and moderation endpoints across users, gigs, jobs, and reports.
- **AI-assisted hiring (Gemini integration)**
  - `GeminiResumeParserImpl` — sends extracted resume text to Gemini with a structured prompt, parses the JSON response into a `ResumeImportPreviewDTO` for one-click resume autofill (with defensive handling of markdown-wrapped JSON responses).
  - `AIInterviewService` — generates and scores AI-driven interview questions per job application (`AIInterviewSession`, `AIInterviewQuestion`).
- **Resume/PDF pipeline** — `ResumeTextExtractor` for parsing uploaded resumes and `PDFResumeService` (PDFBox + OpenHTMLtoPDF) for generating downloadable PDF resumes from structured profile data.
- **Scheduled jobs** — `GigOrderScheduler` runs hourly via `@Scheduled(cron = ...)` to auto-resolve gig orders whose dispute window has expired, keeping order state consistent without manual admin intervention.

### Engineering choices worth highlighting
- **Security retrofit done the safe way:** method-level `@PreAuthorize` was added controller-by-controller across the entire API *before* removing the permissive `permitAll` catch-all — a deliberate, low-risk migration strategy to avoid breaking the Android client mid-development, with a final verification pass to catch any unannotated endpoints.
- **Stateless JWT auth** (`JwtAuthFilter`, `JwtAuthenticationEntryPoint`, `JwtUtil`) integrated with a custom `UserDetailsService` and `DaoAuthenticationProvider`, with `BCryptPasswordEncoder` for password hashing.
- **Route-specific CORS policy** — a dedicated permissive CORS rule scoped only to SSLCommerz's server-to-server payment callback endpoints (`/api/payments/success|fail|cancel`), separated from the credentialed, origin-restricted CORS policy used for the Angular/Android clients — avoiding a blanket CORS relaxation just to support one gateway integration.
- **DTO-first API design** — every entity has paired `RequestDTO`/`ResponseDTO` classes with dedicated mapper classes, keeping JPA entities out of the wire format.
- **Dynamic, type-safe filtering** — JPA `Specification` classes (`GigSpecification`, `JobSpecification`, `UserSpecification`, `TransactionSpecification`, etc.) power admin search/filter endpoints instead of ad-hoc query strings.
- **Global exception handling** via `GlobalExceptionHandler` with custom `UnauthorizedException` / `InvalidCredentialException` for clean, consistent HTTP error responses.
- **API documentation** exposed via springdoc-openapi (Swagger UI) for contract-first collaboration with the frontend/mobile clients.

---

## 2. Web Frontend — `Angular_Frontend/WorkBridgeAngular`

Built on **Angular 21 with standalone components**, organized by feature module rather than by type — each domain (`gig/`, `jobapplication/`, `wallet/`, `conversation/`, `notification/`, `report/`, `saved/`, `dashboard/`) owns its own `components/`, `models/`, and `services/`.

### Core features implemented
- **Role-based layouts** — separate `layout/admin`, `layout/company`, `layout/user`, and `layout/guest` shells, each rendering a different navigation/dashboard experience from the same app.
- **Full auth flow** — login, registration, email verification, forgot/reset password, all backed by dedicated `auth/services` and route `guards` (with an HTTP `interceptor` for attaching the JWT and handling 401s).
- **Admin control center** (`admin-control/`) — dedicated management screens for companies, users, user profiles, gigs, jobs, gig orders, and job applications, each with its own service layer.
- **Freelancer & company dashboards** — `freelancer-dashboard` and `company-dashboard` components consuming purpose-built dashboard DTOs from the backend (`FreelancerDashboardDTO`, `CompanyDashboardDTO`).
- **Gig & job marketplace UX** — browsing, saved gigs/jobs (`saved/`), gig ordering, and job application flows with their own model layers matching backend DTOs one-to-one.
- **Live-feeling messaging & notifications** — `conversation/` chat UI and a `notification/` module with a details view and type-driven routing.
- **Wallet UI** — dedicated `wallet/components` and `wallet/services` for viewing balance, transaction history, and triggering withdrawals/deposits.
- **CV/resume builder** (`admin/cvinformations`, `user/resume`) — structured forms for building a profile that maps directly to the backend's PDF resume generation pipeline.

### Engineering choices worth highlighting
- Consumes a **typed, DTO-mirrored API contract** — Angular models are shaped to match backend response DTOs directly, minimizing translation logic in components.
- **Interceptor-based auth propagation** keeps JWT attachment and refresh/redirect logic out of individual components.
- Styled with **Bootstrap 5 + Bootstrap Icons** for a consistent, responsive UI without a heavier component library, plus **SweetAlert2** for consistent confirmation/error UX across the whole app.

---

## 3. Mobile App — `Android_Frontend_Using_Java/WorkBridgeAndroid`

A **native Android client (Java)** — not a WebView wrapper — implementing the freelancer/job-seeker side of the marketplace, talking to the same Spring Boot API as the Angular app.

### Core features implemented
- **~140 Java files**, 30 activities covering the full user journey: `SplashActivity` → `LoginActivity`/`RegisterActivity`/`ForgotPasswordActivity` → `HomeActivity` → `JobListActivity`/`JobDetailsActivity` → `ApplicationDetailsActivity`/`MyApplications`.
- **Full profile-building suite** on-device: `EducationActivity`, `ExperienceActivity`, `PortfolioActivity`, `TrainingActivity`, `ReferenceActivity`, `UserSkillActivity`, `UserLanguageActivity`, `ExtracurricularActivity` — each with matching `Edit*Activity` counterparts.
- **Cascading address selection** in `UserProfileActivity`, mirroring the backend's `Country → Division → District` hierarchy in the UI.
- **Resume tools on mobile** — `ResumeActivity`, `ResumeFileActivity`, `ResumePreviewActivity` for building, uploading, and previewing resumes/PDFs generated by the backend.
- **AI interview on the go** — `AIInterviewActivity` surfaces the backend's Gemini-powered interview question flow natively.
- **Notifications** — `NotificationActivity` consuming the same notification API/types as the web app.
- **Organized architecture** — dedicated `api/` (Retrofit service interfaces), `repository/`, `request/`/`response/` (DTOs matching backend contracts), `masterdata/`, `adapter/` (RecyclerView adapters), `enums/`, and `session/` (auth/session state) packages, mirroring the backend's DTO-first philosophy on the client side.

### Engineering choices worth highlighting
- **Retrofit + OkHttp** networking layer with a **global Gson configuration** to correctly serialize/deserialize Java `LocalDate`/`LocalDateTime` against the backend — a real interop bug (`HttpMessageNotReadableException`) that was diagnosed and fixed at the `ApiClient` level rather than patched per-call.
- **Glide** for efficient image loading/caching (profile photos, portfolio images, company logos).
- **Lombok** used to keep the request/response DTO layer boilerplate-free, consistent with the backend's own DTO conventions.
- Debugged and resolved a real dependency-conflict issue (PDFBox 3 vs. `openhtmltopdf` groupId) to keep the resume-PDF pipeline working across backend upgrades.

---

## 4. Database — `MySQL_Database_Data/workbridge.sql`

- **50 tables**, fully normalized, including explicit join tables for many-to-many relationships (`categories_jobs`, `categories_skills`, `skills_user_skills`, `languages_user_languages`, `aiinterviewsessions_ai_interview_questions`, and the full geo-hierarchy join tables).
- Ships with **validated seed data** (categories, skills, jobs, gigs) for demo/testing without needing to hand-create marketplace content.

---

## Getting Started

```bash
# 1. Backend
cd SpringBoot_Backend/project
# configure DB credentials + Gemini/SSLCommerz keys in application.properties
./mvnw spring-boot:run

# 2. Web frontend
cd Angular_Frontend/WorkBridgeAngular
npm install
npm start        # http://localhost:4200

# 3. Android app
# open Android_Frontend_Using_Java/WorkBridgeAndroid in Android Studio
# point BASE_URL in ApiClient to your backend (10.0.2.2 for emulator)
```

---

## About the Author

Built solo, end-to-end — backend architecture and security, database design, web frontend, and native mobile client — as a demonstration of full-stack ownership across a non-trivial, real-world marketplace domain.
