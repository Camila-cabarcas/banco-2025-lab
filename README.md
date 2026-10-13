[![CI/CD Pipeline](https://github.com/Camila-cabarcas/banco-2025-lab/actions/workflows/build.yml/badge.svg)](https://github.com/Camila-cabarcas/banco-2025-lab/actions/workflows/build.yml)
[![Coverage Status](https://coveralls.io/repos/github/Camila-cabarcas/banco-2025-lab/badge.svg?branch=main)](https://coveralls.io/github/Camila-cabarcas/banco-2025-lab?branch=main)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Camila-cabarcas_banco-2025-lab&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=Camila-cabarcas_banco-2025-lab)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=Camila-cabarcas_banco-2025-lab&metric=coverage)](https://sonarcloud.io/summary/new_code?id=Camila-cabarcas_banco-2025-lab)

# banco-2025-lab

Banking application (Laboratorio 1 - Arquitectura de Software, UdeA): a Spring Boot REST API (`banco2025`) and a React frontend (`banco-frontend`).

**Live API:** https://banco2025-api.onrender.com

## API endpoints

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/customers` | List customers |
| GET | `/api/customers/{id}` | Get a customer by id |
| POST | `/api/customers` | Create a customer |
| GET | `/api/transactions` | List transactions |
| GET | `/api/transactions/{id}` | Get a transaction by id |
| POST | `/api/transactions` | Transfer money between accounts |
| PUT | `/api/transactions/{id}` | Update a transaction |
| DELETE | `/api/transactions/{id}` | Delete a transaction |

## CI/CD pipeline (GitHub Actions)

`Unit tests → SonarCloud analysis → Build (JAR + Docker image) → Deploy to Render`

* Tests run with an in-memory H2 database; the JaCoCo coverage report is published to SonarCloud and Coveralls.
* The Docker image `camilacabarcas/banco2025` is published to Docker Hub only from `main`.
* Render redeploys automatically through a deploy hook (trunk based: feature branches + pull requests to `main`).

## Database configuration

| Variable | Description | Default (local) |
|---|---|---|
| `DB_URL` | JDBC URL | `jdbc:mysql://127.0.0.1:3306/banco2025` |
| `DB_USERNAME` | Database user | `root` |
| `DB_PASSWORD` | Database password | `root` |
| `PORT` | HTTP port | `8080` |

Locally it uses MySQL; in Render it uses PostgreSQL.

## How to run it locally

```shell
cd banco2025
./mvnw spring-boot:run
```

## How to test it

```shell
cd banco2025
./mvnw clean test
```
