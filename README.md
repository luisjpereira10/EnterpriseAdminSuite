# Enterprise Admin Suite

![CI/CD](https://img.shields.io/badge/CI%2FCD-GitHub%20Actions-blue.svg)
[![Java Version](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4%2B%20%2F%204.x-brightgreen.svg)](https://spring.io/projects/spring-boot)

A modern inventory management web application built with **Spring Boot 4**, **Java 21**, and **Thymeleaf**, designed using a modular layout architecture and clean code principles.

---

## 📋 Overview

Enterprise Admin Suite is a web-based management system
designed to manage users, roles, permissions.

The project is being developed as a portfolio application to demonstrate practical experience
with Java and Spring Boot, with planned features including Spring Security, JPA/Hibernate,
REST APIs, testing, Docker, and CI/CD.

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

* `src/main/java`: MVC Controllers, DTOs, and global configurations (`@ControllerAdvice`).
* `src/main/resources/templates`:
    * `layout.html`: Base layout container.
    * `fragments/`: Reusable components (Sidebar, Navbar, Footer).
    * `users/`, `dashboard/`, `settings/`: Specific module view fragments.

---

## 📌 Development Roadmap

- [x] AdminLTE 4 integration
- [x] Thymeleaf layout
- [x] Reusable fragments
- [x] Navigation
- [x] GitFlow
- [x] GitHub Actions

### v0.2 — Users & Validation
- [ ] User CRUD
- [ ] DTOs
- [ ] Bean Validation
- [ ] Global exception handling

### v0.3 — Persistence
- [ ] PostgreSQL
- [ ] JPA/Hibernate
- [ ] User/Role/Permission entities
- [ ] Database relationships

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

   [http://localhost:8080/\http://localhost:8080/)