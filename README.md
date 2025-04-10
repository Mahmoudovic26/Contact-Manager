
# Java Spring Boot User Registration and Contact Management API

This Java Spring Boot application allows users to register, sign in, and manage their address book. The app provides APIs for user registration, sign-in, and CRUD operations on contacts in the address book.

## Features

- **User Registration**: Users can create an account by providing a valid username, email, and password.
- **User Sign-In**: Users can log in using their credentials and receive a JWT token for authenticated access.
- **Contact Management**: Users can add, list, retrieve, and delete contacts from their address book. Each contact contains:
  - First Name
  - Last Name
  - Phone Number
  - Email Address
  - Birthdate

## Requirements

- **Java 21** (or higher)
- **Spring Boot 3.x**
- **JJWT (JSON Web Token)** for authentication
- **MySQL** (or other relational databases)
- **Docker** (for optional deployment)

## APIs

The application exposes the following endpoints:

### 1. **Create an Account**
- **URL**: `POST /api/auth/register`
- **Request Body**:
  ```json
  {
    "username": "string",
    "email": "string",
    "password": "string"
  }
  ```
- **Response**: `201 Created` or error message
  ```json
    {
        "token": "eyJhbGciOiJIUzI1NiJ9.........",
        "username": "jhondpe"
    }
  ```

### 2. **Sign In**
- **URL**: `POST /api/auth/login`
- **Request Body**:
  ```json
  {
    "username": "string",
    "password": "string"
  }
  ```
- **Response**: `200 OK` with JWT token in the body
  ```json
  {
    "token": "eyJhbGciOiJIUzI1NiJ9.........",
    "username": "jhondpe"
  }
  ```

### 3. **Add a Contact**
- **URL**: `POST /api/contacts`
- **Request Body**:
  ```json
  {
    "firstName": "string",
    "lastName": "string",
    "phoneNumber": "string",
    "email": "string",
    "birthdate": "YYYY-MM-DD"
  }
  ```
- **Response**: `201 Created` or error message

### 4. **List All Contacts**
- **URL**: `GET /api/contacts`
- **Response**:
  ```json
  [
    {
      "id": 1,
      "firstName": "string",
      "lastName": "string",
      "phoneNumber": "string",
      "email": "string",
      "birthdate": "YYYY-MM-DD"
    },
    ...
  ]
  ```

### 5. **Get Contact by ID**
- **URL**: `GET /api/contacts/{id}`
- **Response**:
  ```json
  {
    "id": 1,
    "firstName": "string",
    "lastName": "string",
    "phoneNumber": "string",
    "email": "string",
    "birthdate": "YYYY-MM-DD"
  }
  ```
### 6: Pagination for List Contacts

- **URL:** `GET /api/contacts/page?page=0&size=10&sortBy=lastName&direction=asc`

**Response:**
```json
{
    "contacts": [
        {
            "id": 1,
            "firstName": "Jhon",
            "lastName": "Doe",
            "phoneNumber": "01123456700",
            "email": "jhon@doe.com",
            "birthdate": "2001-10-26"
        }
    ],
    "currentPage": 0,
    "totalPages": 1,
    "totalElements": 1
}
```

### 7. **Delete a Contact**
- **URL**: `DELETE /api/contacts/{id}`
- **Response**: `204 No Content`

## Optional Additional Features

- **Sorting and Pagination** for listing contacts (by first name, last name, or birthdate).
- **Docker Deployment**: Use Docker Compose to deploy the application along with a MySQL database.
- **Unit Tests**: Test core functionalities using JUnit and Mockito.

## Installation

### 1. Clone the repository
```bash
git clone git@github.com:Mahmoudovic26/Contact-Manager.git
cd Contact-Manager
```

### 2. Set up the database
Update your `application.properties` or `application.yml` with the correct database credentials:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/cma
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

### 3. Build and run the application
If you're using Maven, you can run the application using the following command:
```bash
./mvnw spring-boot:run
```

Alternatively, you can build the JAR file and run it:
```bash
./mvnw clean install
java -jar target/CMA-0.0.1-SNAPSHOT.jar
```

The application should now be running at `http://localhost:8080`.

### 4. Run Docker (Optional)
To run the application and database using Docker Compose:
```bash
docker-compose up --build
```

## Postman Collection

You can import the provided Postman collection to test the APIs.

### Instructions:
1. Download the Postman collection file from the repository (or from a separate location if provided).
2. Open Postman and click on the **Import** button.
3. Select the collection file (`CMA_Postman_Collection.json`) and import it.
4. Use the **Sign In** API to obtain a JWT token. Add this token to the Authorization header for subsequent API requests.
   
   Example Authorization header for Postman:
   ```
   Authorization: Bearer YOUR_JWT_TOKEN
   ```

### Available Postman Requests:
1. **Create Account**: `POST /api/auth/register`
2. **Sign In**: `POST /api/auth/login`
3. **Add Contact**: `POST /api/contacts`
4. **List Contacts**: `GET /api/contacts`
5. **Get Contact by ID**: `GET /api/contacts/{id}`
6. **List Page and sort**: `GET /api/contacts/page?page=0&size=10&sortBy=lastName&direction=asc`
7. **Delete Contact**: `DELETE /api/contacts/{id}`

