# 🐾 Pet Service - Spring Boot REST API

This project is a **RESTful API** built with **Spring Boot** for managing pets. It was developed as part of a technical assignment to assess backend development skills in Java.

## 🧩 Technologies & Dependencies

- Java 21
- Spring Boot 3.2.11
- Spring Boot Starter Web
- Spring Data Commons
- Spring Boot Starter
- Spring Boot Starter Validation
- Spring Boot Starter Tests
- Spring Data JPA
- PostgreSQL
- Spring Validation
- MapStruct  
- Lombok  
- Swagger / OpenAPI 2.5.0  
- JUnit 5 + Mockito (for testing)
- Docker

## 📚 Features

- Retrieve all pets (`GET /pets`)
- Retrieve paginated and sorted pets (`GET /pets/paged`)
- Retrieve a pet by ID (`GET /pets/{id}`)
- Create a new pet (`POST /pets`)
- Update an existing pet (`PUT /pets/{id}`)
- Delete a pet (`DELETE /pets/{id}`)

## ✅ Validations & Constraints

- Sorting is only allowed by the following fields: `name`, `species`, `age`
- Custom exceptions are handled, such as `PetNotFoundException` and `SortNotAllowedException`
- Entity-to-DTO and DTO-to-entity mappings are handled using MapStruct
- Timestamps (`insertedDate`, `updatedDate`) are automatically managed in the service layer

## 🧪 Test Coverage

Unit tests are written using JUnit 5 and Mockito. They cover:

- Success and failure scenarios for all service methods  
- Validation of allowed and disallowed sorting fields  
- Mapping logic and internal business logic of `PetServiceImpl`

To run the tests:

```bash
mvn test
```
## ▶️ Running the Application

You can run the application using one of the following methods:

- **Using Maven**:

```bash
mvn spring-boot:run
```
- **Using IntelliJ IDEA**:
Open the project in IntelliJ, locate the main class (PetServiceApplication), and run it directly from the IDE.

## 📦 Populate the Database with Sample Data

To populate the Database I used https://www.mockaroo.com/ and we can see this data in /resources/mock_data.json

## 🚀 Running with PostgreSQL (JPA profile)

If we use the **jpa** profile, the application will connect to the real PostgreSQL database running inside Docker.  
To start Docker, run the following command in the `/docker` directory:

```bash
docker compose up
```
To access the data inside the container, connect by running:
```bash
docker exec -it petdb-postgres bash
```
Then, connect to the database with:
```bash
psql -U postgres -d petdb
```
Finally, you can query your data using:
```sql
SELECT * FROM pet;
```
## 📖 API Documentation with Swagger

The API documentation is available through **Swagger UI**, which provides an interactive interface to explore and test the endpoints.

### How to access Swagger UI

1. Make sure the application is running.
2. Open your web browser and navigate to:
http://localhost:8080/swagger-ui/index.html

   
