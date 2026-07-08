# Scalable Android App Architecture (2026 Edition)

This project follows a **Scalable Android App Architecture** combining **Clean Architecture**, **MVVM (Model-View-ViewModel)**, and **Feature-Based Modularization**. It is designed to be highly scalable, maintainable, testable, and team-friendly.

---

## 1. Core Principles & Best Practices
* **Separation of Concerns:** Distinct separation of responsibilities into `Presentation`, `Domain`, and `Data` layers.
* **Unidirectional Data Flow (UDF):** `View (Compose Screen)` $\rightarrow$ sends `UI Event` $\rightarrow$ `ViewModel` $\rightarrow$ invokes `Use Case` $\rightarrow$ queries `Repository` $\rightarrow$ updates `UI State` $\rightarrow$ renders in `View`.
* **Feature Independence:** Keep feature modules decoupled and independent. Expose only what is strictly necessary using an API/public visibility layer.
* **Dumb UI & Business Isolation:** Keep the UI layer purely focused on rendering. All business logic must reside within the domain layer's Use Cases.
* **Testability:** Interface-driven design allowing easy mocking with JUnit and MockK.

---

## 2. Tech Stack (2026)
* **UI Layer:** Jetpack Compose (Material Design 3)
* **Dependency Injection:** Hilt
* **Asynchrony & Reactivity:** Kotlin Coroutines + Flow (`StateFlow`, `SharedFlow`)
* **Navigation:** Navigation Compose (Type-safe using `kotlinx.serialization`)
* **Network:** Retrofit + OkHttp
* **Local Database:** Room Database
* **Key-Value Storage:** DataStore
* **Image Loading:** Coil
* **Testing:** JUnit + MockK

---

## 3. Project Structure & Scaling Strategy

Depending on app complexity, the module architecture scales across four stages:
1. **Small/Medium App (<50 Screens):** Single module (`app/`) structured via sub-folders (`core/` and `feature/`).
2. **Large/Enterprise App (>50 Screens):** Multi-module architecture where core components and individual features are split into standalone Gradle modules (`core-network`, `feature-home`, etc.).

### High-Level Directory Overview
```text
app/
├── core/                → Shared, reusable components across features
│   ├── designsystem/    → Theme, Typography, Colors, Shared UI Components
│   ├── ui/              → Base UI components, extensions
│   ├── navigation/      → NavHost, Destinations, Navigator
│   ├── network/         → Retrofit, OkHttp, Interceptors
│   ├── database/        → Room database, DAOs
│   ├── datastore/       → DataStore preferences
│   ├── common/          → Utils, Constants, Extensions, Resources
│   └── testing/         → Testing utilities, fakes, rules
├── feature/             → Feature modules (Package or Module by Domain)
│   ├── auth/
│   ├── home/
│   ├── search/
│   ├── profile/
│   └── settings/
└── buildSrc/            → Build logic and custom plugins (or Version Catalogs)


home/
├── data/ (Data Layer)
│   ├── remote/          → API Interfaces, Data Transfer Objects (DTOs)
│   ├── repository/      → Repository Implementations
│   └── mapper/          → Mapping logic (DTO ↔ Domain Model)
│
├── domain/ (Domain Layer)
│   ├── model/           → Pure Domain Models (Business Models)
│   ├── repository/      → Repository Interfaces (Abstraction layer)
│   └── usecase/         → Single Responsibility Business Logic (Use Cases)
│
└── presentation/ (Presentation Layer - MVVM)
    ├── screen/          → Jetpack Compose Screens (Views)
    ├── component/       → Reusable feature-specific UI elements
    ├── state/           → UI State data classes (ViewModel State)
    ├── event/           → UI Events / User Actions (e.g., Click events)
    ├── effect/          → One-off side effects (Navigation, Toasts, Dialogs)
    └── viewmodel/       → ViewModels handling state transitions


 ┌────────────────────────────────────────────────────────┐
 │                                                        │
 ▼                                                        │
COMPOSE SCREEN (View) ──[ User Event ]──► VIEWMODEL (State/Flow)
 ▲                                                │
 │                                         [ Calls Use Case ]
[ UI State Update ]                               │
 │                                                ▼
VIEWMODEL ◄──[ Result Data ]────────────── USE CASE (Domain)
                                                  │
                                         [ Calls Repository ]
                                                  │
                                                  ▼
DATA SOURCE (API/DB) ◄───[ Data ]───────── REPOSITORY (Data)