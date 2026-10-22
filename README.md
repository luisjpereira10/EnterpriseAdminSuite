# 📦 Enterprise Admin Suite

![CI/CD](https://img.shields.io/badge/CI%2FCD-GitHub%20Actions-blue.svg)
[![Java Version](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4%2B%20%2F%204.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
![License](https://img.shields.io/badge/License-MIT-blue.svg)

A modern inventory management web application built with **Spring Boot 4**, **Java 21**, and **Thymeleaf**, designed using a modular layout architecture and clean code principles.

---

## 🚧 Work in Progress & Active Evolution

This application is actively being built and refined. Features are deployed incrementally across scheduled release milestones (see [Development Roadmap](#-development-roadmap)).

* 🎯 **Current Phase:** `v0.3 — Testing Suite & Coverage` (In Progress)
* 🏷️ **Latest Tag:** [`v0.2`](#-v02--users-validation--security-baseline) — Users, Validation & Security Baseline (Completed)
* 🔄 **Update Frequency:** Continuous commits based on GitFlow feature branches.
* 🏛️ **Architecture:** Evolving toward REST APIs, Spring Security authentication, and Docker containerization.
---

## 📋 Overview

Enterprise Admin Suite is a web-based management system for managing users, roles, and permissions.

Developed as a portfolio project to demonstrate practical skills in Java and Spring Boot, the application follows a layered architecture and focuses on maintainability, validation, and secure access control.

Planned features include REST APIs, automated testing, PostgreSQL integration, Docker containerization, and CI/CD workflows.

---
## 🛠️ Tech Stack & Tooling

* **Language:** Java 21 (LTS)
* **Framework:** Spring Boot 4.x
* **Template Engine:** Thymeleaf (Dynamic Layout Pattern)
* **UI & Styling:** AdminLTE 4 (Bootstrap 5) & Bootstrap Icons
* **CI/CD:** GitHub Actions (Automated Maven Build & Testing pipeline)
* **Branching Strategy:** GitFlow (`main`, `develop`, `feature/*`)
* **Versioning & Commits:** Conventional Commits

---

## ⚙️ Continuous Integration (GitHub Actions)

This project features an automated CI pipeline that builds and verifies the code on pushes to `main`, `develop`, and `feature/*` branches, as well as on pull requests targeting `main` or `develop`.

* **Runner:** `ubuntu-latest`
* **JDK Distribution:** Eclipse Temurin 21
* **Build Tool:** Maven Wrapper (`./mvnw clean verify`)

---

## 📁 Project Architecture & Layout

```text
src/main/java/com/josepereira/inventory_admin_suite
├── config
│   ├── DataInitializer.java
│   ├── GlobalModelAttributes.java
│   └── SecurityConfig.java
├── controller
│   ├── AuthController.java
│   ├── DashboardController.java
│   ├── SettingsController.java
│   └── UserController.java
├── dto
│   ├── UserCreateDTO.java
│   └── UserUpdateDTO.java
├── entity
│   └── User.java
├── exception
│   ├── GlobalExceptionHandler.java
│   └── ResourceNotFoundException.java
├── repository
│   └── UserRepository.java
├── service
│   ├── impl/
│   └── UserService.java
└── InventoryAdminSuiteApplication.java 
```
---

## 📌 Development Roadmap

## 🎯 Product Milestones & Vision

| Versión | Título de la Versión | Estado |
| :--- | :--- | :---: |
| **v1.0** | **`🏛️ v1.0 — Monolithic SSR Edition (Thymeleaf & AdminLTE)`** | 🟡 *In Progress (v0.3)* |
| **v2.0** | **`🅰️ v2.0 — Decoupled SPA Architecture (Angular Frontend)`** | ⚪ *Planned* |
| **v3.0** | **`⚛️ v3.0 — Modern Full-Stack Ecosystem (React Frontend)`** | ⚪ *Planned* |

---

## 🏛️ v1.0 — Detailed Execution Plan (Thymeleaf & AdminLTE Baseline)

### 🎨 v0.1 — UI Architecture & Infrastructure Baseline
- [x] AdminLTE 4 integration
- [x] Thymeleaf layout & reusable fragments
- [x] Navigation bar & sidebar components
- [x] GitFlow workflow branching model
- [x] GitHub Actions CI Pipeline (Automated Java 21 Temurin setup, Maven build & verify on push/PR)

### 🛡️ v0.2 — Users, Validation & Security Baseline
- [x] H2 Database & Spring Data JPA integration
- [x] User Entity & Repository layer
- [x] User CRUD implementation (Controller & Service layers)
- [x] DTO pattern separation (`UserCreateDTO` & `UserUpdateDTO`)
- [x] Jakarta Bean Validation on DTOs
- [x] Data sanitization & Business Uniqueness validation
- [x] Password encoding with Spring Security (BCrypt)
- [x] Centralized error handling (`@ControllerAdvice` & `@ExceptionHandler`)
- [x] UI alerts and User Modals integration

### 🧪 v0.3 — Testing Suite & Coverage
- [x] Global Exception Handling setup
- [x] Custom Exception implementation (`ResourceNotFoundException`)
- [x] Unit Testing for `UserService` using Mockito & JUnit 5
- [ ] Web Layer Testing for `UserController` using `@WebMvcTest`
- [ ] Integration & End-to-end verification (`@SpringBootTest`)
- [ ] Automated Test pipeline execution in GitHub Actions CI

### 🌐 v0.4 — REST API, OpenAPI/Swagger & Security
- [ ] Custom Spring Security configuration (Form Login, Sessions & Access Control)
- [ ] Role & Permission entities (`RBAC` Architecture)
- [ ] RESTful API endpoints implementation (`@RestController`)
- [ ] OpenAPI 3 / Swagger UI integration for interactive API documentation
- [ ] JSON response standard & HTTP status codes optimization
- [ ] PostgreSQL integration for Production profile

### 🐳 v0.5 — Containerization & Production Release
- [ ] Multi-stage `Dockerfile` creation (JDK 21)
- [ ] `docker-compose.yml` setup for App + PostgreSQL
- [ ] GitHub Actions CI/CD workflow to publish Docker image to Docker Hub
- [ ] **🚀 Official v1.0 Tag & Release (Monolithic SSR Edition completed)**

---

## 🅰️ v2.0 — Decoupled SPA Architecture (Angular Frontend)
- [ ] Angular project setup & module structure
- [ ] Authentication & JWT Token Handling
- [ ] AdminLTE template integration with Angular Components
- [ ] Consumption of v1.0 RESTful APIs

---

## ⚛️ v3.0 — Modern Full-Stack Ecosystem (React Frontend)
- [ ] React + TypeScript application setup
- [ ] State Management (Redux Toolkit or React Query)
- [ ] Modern UI Dashboard implementation
- [ ] Full REST API integration & End-to-End testing
---

## 🚀 Getting Started

### Prerequisites

* JDK 21 installed.
* Maven wrapper included in project.

### Running Locally

1. **Clone the repository:**

```bash
git clone https://github.com/luisjpereira10/EnterpriseAdminSuite.git
cd EnterpriseAdminSuite
```

2. **Run the application:**

```bash
./mvnw spring-boot:run
```

3. **Access the application:**

   [http://localhost:8080](http://localhost:8080)