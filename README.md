# 📝 The Notes App (Test Project)

A robust Spring Boot and MongoDB RESTful API for managing everyday notes. This project demonstrates high-quality backend development practices, including containerization, data validation, and automated text analysis.

## 🚀 Technology Stack
* **Java 21**
* **Spring Boot 3.x** (Spring Data MongoDB, Spring Web, Validation)
* **MongoDB** (Configured with Replica Set for transactional support)
* **Lombok** (Builder, Data, RequiredArgsConstructor)
* **Docker & Docker Compose**

## 🛠 Getting Started

### 1. Prerequisites
Ensure you have **Docker** and **Docker Compose** installed on your machine.

### 2. Infrastructure Setup (MongoDB)
The project requires a MongoDB Replica Set. This is handled automatically by the provided Docker Compose configuration.

```bash
docker-compose up -d
```
Note: Wait ~10 seconds after startup for the replica set initialization script (rs.initiate) to complete.

3. Run the Application

You can run the application using Maven:

```bash
./mvnw spring-boot:run
```
📡 REST API Endpoints
Note Management:

POST /note/add – Create a new note. title and text are mandatory.

PUT /note/{id} – Update an existing note by ID.

DELETE /note/delete/{id} – Delete a specific note.

GET /note/get/{id} – Retrieve full note details (Card View).

Search & Analytics:

POST /note/search?page=0&size=3 – List notes (Title + Date) with pagination and tag filtering.

GET /note/statistic/{id} – Calculate unique word frequency in a note's text (Sorted descending).

🧪 Testing
A comprehensive http.http file is included in the project root. This file is designed for the IntelliJ HTTP Client and allows you to test the entire lifecycle of a note (Create -> Update -> Stats -> Delete) with automatic variable substitution.

💡 Implementation Details
Strict Validation: Uses @NotBlank to ensure that titles and texts are not just non-null, but also contain meaningful characters (preventing empty strings or whitespace).

MongoDB Replica Set: Configured within Docker to support advanced MongoDB features and simulate a production-ready environment.

DTO Pattern: Implements a clear separation between database entities and API request/response objects for better security and maintainability.

Text Analytics: The statistics engine correctly handles punctuation and case sensitivity to provide accurate word counts.

Developer: Dima Sydorenko — Java Developer