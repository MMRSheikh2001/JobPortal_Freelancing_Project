# WorkBridge — Full-Stack Job Portal & Freelance Marketplace

A full-stack marketplace platform combining a **traditional job portal** (companies post jobs, candidates apply, AI screens and shortlists) with a **Fiverr-style gig marketplace** (freelancers sell productized services, clients place orders). Built end-to-end as a solo project: a Spring Boot REST API, an Angular SPA, and a native Android client, all backed by a single normalized MySQL schema, with in-app payments, wallets, messaging, notifications, and AI-assisted hiring tools.

> **Repo layout:** this is a monorepo containing three independently deployable applications that share one backend contract.
> - `SpringBoot_Backend/project` — REST API (Java / Spring Boot)
> - `Angular_Frontend/WorkBridgeAngular` — web client (Angular)
> - `Android_Frontend_Using_Java/WorkBridgeAndroid` — native mobile client (Java)
> - `MySQL_Database_Data/workbridge.sql` — full schema + seed data (50 tables)

---

 ▶️ [Watch Demo Video](https://www.youtube.com/watch?v=yzHAcHdAU7k)

## What's implemented

- **Two marketplace models on one backend** — a job portal (`Job` / `JobApplication`, company-posted, deadline- and vacancy-driven) and a gig marketplace (`Gig` / `GigOrder`, freelancer-posted, order-lifecycle-driven), sharing the same user, category, payment, and review infrastructure.
- **AI-assisted hiring pipeline for the job portal** — companies can enable per-job AI CV screening, a configurable match-score threshold, and an AI interview stage; applications move through an `AI_PENDING → AI_COMPLETED → AUTOMATIC_QUALIFIED / COMPANY_SHORTLISTED` status flow driven by Gemini scoring, not just manual review.
- **A self-written job recommendation engine** — `RecommendedJobService` scores active jobs against a candidate's profile (location match, employment/workplace type match, skill-text match) and returns a ranked top-10, independent of the Gemini calls used elsewhere.
- **Three clients on one contract** — the same Spring Boot API serves the Angular web app and the native Android app.
- **JWT auth + method-level authorization** — Spring Security `@PreAuthorize` with ownership checks across 40+ REST controllers (a freelancer can only edit their own resume, a company can only manage its own job postings, etc.).
- **Payment integration** — SSLCommerz gateway with callback verification, wallet crediting, and a transaction/withdrawal ledger.
- **Scheduled jobs** (Spring `@Scheduled`) — e.g. hourly auto-resolution of gig orders whose dispute window has expired.

---

 ▶️ [Watch Demo Video](https://www.youtube.com/watch?v=yzHAcHdAU7k)

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

All three clients call the same REST contract; CORS and JWT are configured to serve `localhost:4200` (Angular dev server) and `10.0.2.2` (Android emulator) at the same time.

---

## 1. Backend — `SpringBoot_Backend/project`

**~390 Java files** organized by domain module (not generic layers), e.g. `gig/`, `gigorder/`, `jobapplication/`, `payment/`, `wallet/`, `withdraw/`, `conversation/`, `notification/`, `review/`, `report/`, `ai/`, `sslcommerz/`, `security/` — each with its own controller, service/serviceImpl, repository, entity, mapper, and request/response DTOs.

### Job portal features
- **Job postings** (`Job` entity) — companies post jobs with description, responsibilities, education/experience requirements, salary range (with negotiable flag), employment type, workplace type (remote/onsite/hybrid), vacancy count, application deadline, category, and a location tied into the address hierarchy. `JobController` exposes full CRUD plus filtered listings (by company, active/inactive, top-10/top-20 recent, dynamic search) and a status-toggle endpoint.
- **Applications & lifecycle** (`JobApplication` entity) — a candidate applies once per job; status moves through `APPLIED → AI_PENDING → AI_COMPLETED → AUTOMATIC_QUALIFIED / COMPANY_SHORTLISTED → HIRED / REJECTED / WITHDRAWN`, with company notes and AI scores stored alongside.
- **Per-job AI screening configuration** — each `Job` carries its own `aiScreeningEnabled`, `aiCvScreeningEnabled`, `aiInterviewEnabled`, `aiMatchThreshold`, `aiQuestionCount`, `aiShortlistCount`, and `aiDeadlineDays`, so a company can turn AI screening on/off and tune it per posting rather than it being a global switch.
- **AI CV screening** (`ResumeScreeningServiceImpl`) — on application, if AI screening is enabled for the job, the candidate's generated resume and the job requirements are sent to Gemini for a match score + written feedback; applications above the configured threshold are auto-advanced to `AI_PENDING` with an AI interview deadline set automatically.
- **AI interview stage** (`AIInterviewService`, `AIInterviewController`) — generates interview questions for qualifying applications and records session results (`AIInterviewSession`, `AIInterviewQuestion`), feeding into the application's final AI score.
- **Job recommendations without AI** (`RecommendedJobService`) — a rule-based scoring function ranks active jobs for a candidate using location proximity (same police station / same district), employment-type match, workplace-type match, and keyword overlap between the candidate's skills and the job text, capped at 100 and returning the top 10.
- **Company-side hiring tools** — `company-job-applications` (Angular) and the matching backend endpoints let a company review applicants per job, see AI match scores/feedback, and shortlist manually where AI screening isn't used.

### Gig marketplace features
- **Gigs & orders** — freelancer-posted `Gig` listings with `GigOrder` lifecycle (placed → in-progress → delivered → disputed/completed), independent of the job-application flow above.
- **Company & freelancer profiles** — `Education`, `Experience`, `Portfolio`, `Training`, `Reference`, `Extracurricular`, `UserSkill`, `UserLanguage`, plus `CompanyProfile` with public company pages.
- **Geo-hierarchical address system** — `Country → Division → District → PoliceStation → Address`, modeled as a relational hierarchy rather than flat text fields, and reused by both the job-location and address-book features.
- **In-app wallet & payments** — `Wallet`, `Transaction`, `Payment`, and `Withdraw` entities: SSLCommerz-initiated deposits, wallet crediting on payment success, and a separate withdrawal request/approval pipeline.
- **Messaging & notifications** — `Conversation`/`Message` chat between clients and freelancers, plus a `Notification` system with type-based routing (new message, application status change, payment events, etc.).
- **Reviews & moderation** — `Review` system for completed orders/jobs, plus a `Report` pipeline with `ReportStatus`/`ReportType` for admin review.
- **Admin operations** — `AdminDashboardDTO`, user search via JPA `Specification` (dynamic filtering), and moderation endpoints across users, gigs, jobs, and reports.
- **Resume/PDF pipeline** — `ResumeTextExtractor` for parsing uploaded resumes, `GeminiResumeParserImpl` for AI-assisted resume autofill from an uploaded file, and `PDFResumeService` (PDFBox + OpenHTMLtoPDF) for generating downloadable PDF resumes from structured profile data.
- **Scheduled jobs** — `GigOrderScheduler` runs hourly via `@Scheduled` to auto-resolve gig orders whose dispute window has expired.

### Engineering notes
- **Security migration approach:** method-level `@PreAuthorize` was added controller-by-controller across the API before removing the permissive `permitAll` catch-all, to avoid breaking the Android client mid-development, with a final pass to check for unannotated endpoints.
- **Stateless JWT auth** (`JwtAuthFilter`, `JwtAuthenticationEntryPoint`, `JwtUtil`) with a custom `UserDetailsService`, `DaoAuthenticationProvider`, and `BCryptPasswordEncoder`.
- **Route-specific CORS** — SSLCommerz's server-to-server payment callback endpoints (`/api/payments/success|fail|cancel`) get their own permissive CORS rule, kept separate from the credentialed, origin-restricted policy used for the Angular/Android clients.
- **DTO-first API design** — paired `RequestDTO`/`ResponseDTO` classes with dedicated mappers per entity, keeping JPA entities out of the wire format.
- **Dynamic filtering** via JPA `Specification` classes (`GigSpecification`, `JobSpecification`, `UserSpecification`, `TransactionSpecification`, etc.) for admin search endpoints.
- **Centralized exception handling** via `GlobalExceptionHandler` with custom `UnauthorizedException` / `InvalidCredentialException`.
- **API docs** via springdoc-openapi (Swagger UI).

---

## 2. Web Frontend — `Angular_Frontend/WorkBridgeAngular`

Built on **Angular 21 with standalone components**, organized by feature module rather than by type — each domain (`gig/`, `jobapplication/`, `wallet/`, `conversation/`, `notification/`, `report/`, `saved/`, `dashboard/`) owns its own `components/`, `models/`, and `services/`.

### Job portal UI
- **Job applications** (`jobapplication/`) — `my-applications` (candidate's applied jobs and their status), `company-job-applications` (company's view of applicants per posting), `application-details`, plus dedicated `ai-evaluation` and `ai-interview` components that surface the backend's AI match score, feedback, and interview flow directly in the UI.
- **Admin job management** (`admin-control/admin-job-management`) — moderation view over all job postings.
- **Job browsing & saved jobs** — job listing/detail views plus `saved/my-saved-jobs` for candidates bookmarking postings.

### Gig marketplace & general UI
- **Role-based layouts** — separate `layout/admin`, `layout/company`, `layout/user`, and `layout/guest` shells, each with its own navigation/dashboard.
- **Full auth flow** — login, registration, email verification, forgot/reset password, backed by `auth/services` and route `guards`, with an HTTP `interceptor` for attaching the JWT and handling 401s.
- **Admin control center** (`admin-control/`) — management screens for companies, users, user profiles, gigs, gig orders, and (as above) job applications, each with its own service layer.
- **Freelancer & company dashboards** — `freelancer-dashboard` and `company-dashboard` components consuming purpose-built dashboard DTOs from the backend (`FreelancerDashboardDTO`, `CompanyDashboardDTO`).
- **Gig marketplace UX** — browsing, saved gigs, and gig ordering flows with model layers matching backend DTOs.
- **Messaging & notifications** — `conversation/` chat UI and a `notification/` module with a details view and type-driven routing.
- **Wallet UI** — `wallet/components` and `wallet/services` for viewing balance, transaction history, and triggering withdrawals/deposits.
- **CV/resume builder** (`admin/cvinformations`, `user/resume`) — structured forms feeding the backend's PDF resume generation pipeline.

### Engineering notes
- Angular models are shaped to match backend response DTOs directly, keeping translation logic out of components.
- An HTTP interceptor handles JWT attachment and 401 redirects in one place rather than per-component.
- Styled with Bootstrap 5 + Bootstrap Icons, with SweetAlert2 for confirmation/error dialogs.

---

## 3. Mobile App — `Android_Frontend_Using_Java/WorkBridgeAndroid`

A native Android client (Java) covering the candidate/freelancer side of the marketplace, talking to the same Spring Boot API as the Angular app.

### Job portal on Android
- **`JobListActivity`** — browse and search job postings, backed by `JobRepository` and `JobSearchRequestDTO`, rendered via `JobAdapter`.
- **`JobDetailsActivity`** — full job detail view (description, requirements, salary, deadline) with an apply action, using `JobApplicationRequestDTO`/`JobApplicationResponseDTO`.
- **`ApplicationDetailsActivity` / `MyApplications`** — candidate's submitted applications and their status.
- **`AIInterviewActivity`** — the Gemini-powered interview flow, native rather than a web view.

### Gig marketplace & general app
- **30 activities, ~140 Java files** covering the full user journey: `SplashActivity` → `LoginActivity`/`RegisterActivity`/`ForgotPasswordActivity` → `HomeActivity` → job/gig browsing → applications/orders.
- **Full profile-building suite** on-device: `EducationActivity`, `ExperienceActivity`, `PortfolioActivity`, `TrainingActivity`, `ReferenceActivity`, `UserSkillActivity`, `UserLanguageActivity`, `ExtracurricularActivity`, each with a matching `Edit*Activity`.
- **Cascading address selection** in `UserProfileActivity`, mirroring the backend's `Country → Division → District` hierarchy.
- **Resume tools** — `ResumeActivity`, `ResumeFileActivity`, `ResumePreviewActivity` for building, uploading, and previewing resumes/PDFs generated by the backend.
- **Notifications** — `NotificationActivity` consuming the same notification API/types as the web app.
- **Package layout** — `api/` (Retrofit service interfaces), `repository/`, `request/`/`response/` (DTOs matching backend contracts), `masterdata/`, `adapter/` (RecyclerView adapters), `enums/`, `session/` (auth/session state).

### Engineering notes
- Retrofit + OkHttp for networking, with a global Gson configuration added to correctly serialize/deserialize Java `LocalDate`/`LocalDateTime` against the backend — this fixed a real `HttpMessageNotReadableException` at the `ApiClient` level rather than patching it per call.
- Glide for image loading/caching (profile photos, portfolio images, company logos).
- Lombok in the request/response DTO layer, matching the backend's own DTO conventions.
- Resolved a PDFBox 3 / `openhtmltopdf` groupId dependency conflict to keep the resume-PDF pipeline working.

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

# WorkBridge Flutter

WorkBridge Flutter is the cross-platform mobile and web client for **WorkBridge**, a full-stack job marketplace and freelance platform. It consumes the existing Spring Boot REST API and provides role-based features for job seekers and gig buyers.

## 🚀 Overview

The Flutter application is designed to provide a modern, responsive client for the WorkBridge platform.

It communicates with the WorkBridge Spring Boot backend through REST APIs and uses JWT-based authentication for secure access.

### Current Flutter Scope

* **Job Seeker**

  * Registration and login
  * Email verification
  * Password recovery
  * Job browsing and searching
  * Job details
  * Job applications
  * User profile management
  * Education, experience, skills, languages, training and portfolio management
  * Resume generation
  * Resume file upload and import
  * AI job matching / interview features

* **Gig Buyer**

  * Browse gigs
  * View gig details
  * Place orders
  * Manage orders
  * Delivery and related gig features

The application follows the existing WorkBridge backend design rather than introducing a separate backend.

---

## 🛠️ Technology Stack

### Frontend

* **Flutter**
* **Dart**
* Material Design
* Riverpod for state management
* Dio for HTTP communication
* Flutter Secure Storage for session/token storage

### Backend

The Flutter application consumes the existing WorkBridge backend:

* Java
* Spring Boot
* Spring Security
* JWT Authentication
* Spring Data JPA
* Hibernate
* MySQL
* REST API

### Development Tools

* Android Studio
* Visual Studio Code
* Git
* GitHub
* Postman

---

## 🏗️ Architecture

The project follows a feature-oriented structure with separation between UI, state management, repositories, models and API communication.


The application uses:

```text
Screen
   ↓
Provider / Controller
   ↓
Repository
   ↓
ApiClient (Dio)
   ↓
Spring Boot REST API
   ↓
MySQL
```

---

## 🔐 Authentication

WorkBridge Flutter uses JWT authentication.

The authentication flow is:

```text
Register
   ↓
Email Verification
   ↓
Login
   ↓
JWT Token
   ↓
Secure Storage
   ↓
Authenticated API Requests
```

The `ApiClient` automatically retrieves the stored JWT and attaches it to authenticated requests:

```text
Authorization: Bearer <JWT>
```

Unauthorized responses are handled centrally by the API client.

---

## 🌐 API Configuration

The API base URL is configured through `ApiConstants`.

For example:

```text
http://localhost:8090/api/
```

The application automatically determines the appropriate host depending on the platform.

### Android Emulator

```text
10.0.2.2
```

This allows the Android emulator to access the host machine's localhost.

### Flutter Web

```text
localhost
```

### iOS Simulator

```text
localhost
```

---

## 📱 Main Features

### Authentication

* User registration
* Login
* Email verification
* Forgot password
* Reset password
* JWT session management
* Automatic logout on unauthorized requests

### Job Marketplace

* Search jobs
* Filter jobs
* View job details
* Apply for jobs
* Track applications

### User Profile

* Personal information
* Contact information
* Present and permanent address
* Professional summary
* Career objective
* Social links
* Salary expectations
* Job preferences

### CV Information

The application supports management of:

* Education
* Work experience
* Skills
* Languages
* Training
* Portfolio
* References
* Extracurricular activities

### Resume

* Generate resume
* View resume
* Generate PDF resume
* Upload existing resume
* Import resume information
* Preview imported information before saving

### Freelance Marketplace

For the gig buyer side:

* Browse gigs
* View gig details
* Place orders
* Manage orders
* Handle gig deliveries

---

## 🤖 AI Features

WorkBridge integrates AI-assisted functionality through the existing backend.

The Flutter client can communicate with the AI job matching/interview functionality provided by the Spring Boot API.

This allows job seekers to receive job-related matching and interview assistance based on their profile and selected job.

---

## 📂 API Modules

The Flutter client communicates with several WorkBridge API modules:

```text
Authentication
Dashboard
Jobs
User Profiles
Company Profiles
Education
Experience
Skills
Languages
Training
Portfolio
References
Extracurricular Activities
Resume
Resume Import
Uploaded Resume Files
Gigs
Orders
Messages
Wallet
Payments
```

---

## 🔄 Resume Import Flow

The resume import feature follows this workflow:

```text
Upload Resume
      ↓
Spring Boot Resume Parser
      ↓
Resume Import API
      ↓
Flutter Preview
      ↓
User Reviews Information
      ↓
Save Imported Information
      ↓
User Profile / CV Sections Updated
```

The Flutter application receives a `ResumeImportPreviewDTO` containing the extracted profile and CV information.

---

## ▶️ Running the Project

### 1. Clone the repository

```bash
git clone https://github.com/MMRSheikh2001/JobPortal_Freelancing_Project.git
```

### 2. Open the Flutter project

Open the Flutter project directory in Android Studio or Visual Studio Code.

### 3. Install dependencies

```bash
flutter pub get
```

### 4. Start the Spring Boot backend

Make sure the WorkBridge Spring Boot backend is running on:

```text
http://localhost:8090
```

### 5. Run Flutter

For web:

```bash
flutter run -d chrome
```

For Android:

```bash
flutter run
```

---

## 🔧 Important Configuration

Before running the application, make sure:

* The Spring Boot backend is running.
* MySQL is running.
* The backend database is configured correctly.
* The API port is `8090`.
* CORS is configured correctly for Flutter Web.
* An Android emulator or physical device can reach the backend when running on Android.

For a physical Android device, `localhost` or `10.0.2.2` should not be used to reach the development PC. The computer's local network IP should be configured instead.

---

## 📌 Project Status

The Flutter client is an ongoing cross-platform implementation of the WorkBridge platform.

The primary goal is to provide a modern Flutter client while reusing the completed Spring Boot backend and its existing REST API architecture.

---

## 👨‍💻 Developer

**Md. Mahbubur Rahman Sheikh**

WorkBridge was developed as part of the **ISDB-BISEW IT Scholarship Programme — Diploma in Web and Mobile App Development using Spring Boot, Android & Flutter**.

### Related Technologies

```text
Spring Boot + MySQL
        ↓
     REST API
        ↓
      Flutter
        ↓
 Android / iOS / Web
```

---

## 📄 License

This project is developed as an educational and portfolio project.
