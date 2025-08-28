# Spring Boot Project with JWT Security
This is a Spring Boot application that demonstrates:
- RESTful API development
- Database integration with JPA and MySQL
- Authentication and authorization using Spring Security with JWT
- Layered architecture (Controller → Service → Repository)

## Features
- **User authentication** using JWT (JSON Web Token)
- **Role-based authorization**
- **CRUD APIs** for your main entities (e.g., Events, Users)
- **Database auto-creation** with `spring.jpa.hibernate.ddl-auto`

## Tech Stack
- **Java 17** / **Spring Boot 3**
- **Spring Security**, **JWT**
- **Spring Data JPA**, **Hibernate**
- **MySQL** (or any relational DB)
- **Maven** build tool

## Getting Started

### Prerequisites
- Java 17+
- Maven 3+
- MySQL server running locally

### Setup
1. **Clone the repository**
   git clone https://github.com/your-username/your-repo-name.git
   cd your-repo-name
   
2. **Configure Database**
Update src/main/resources/application.properties:
```
spring.datasource.url=jdbc:mysql://localhost:3306/your_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=root
```

3. **Run the application**

```mvn spring-boot:run ```

4.**Test API endpoints**
- Public endpoints: POST /api/auth/login, POST /api/auth/register
- Protected endpoints: JWT Required
  - GET /api/events (ROLE_ADMIN,ROLE_USER),
  - GET /api/events/:id (ROLE_ADMIN,ROLE_USER),
  - POST /api/events (ROLE_ADMIN)
  - PUT /api/events/:id (ROLE_ADMIN)
  - DELETE /api/events/:id (ROLE_ADMIN)

5. **Project Structure**
- src/main/java/com/example/project
-  ├── config          # Security configuration (JWT filters, WebSecurityConfig)
-  ├── controller      # REST Controllers
-  ├── dto             # Request/Response DTOs
-  ├── entity          # JPA entities
-  ├── repository      # JPA repositories
-  ├── security        # JWT utilities and filter
-  ├── service         # Business logic
-  └── Application.java

6. **API Documentation**

**NOTE :- I have used POSTMAN for API Testing.**  

**(i) Register User**  
POST https://localhost:7070/users/signup  
Request :  
```
{
  "firstName":"ankit",
  "lastName":"mittal",
  "email": "ankit@gmail.com",
  "password": "1234",
  "role": "ROLE_ADMIN"
}
```
Response :  
```
{
    "message": "User registered successfully!",
    "userId": 1
}
```

**(ii) SignIn to get the token for protected routes**  
POST https://localhost:7070/users/signin  
Request :  
```
{
    "email":"ankit@gmail.com",
    "password":"1234"
}
```
Response :  
```
{
    "jwt": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhbmtpdEBnbWFpbC5jb20iLCJpYXQiOjE3NTYzODMyMjMsImV4cCI6MTc1NjQ2OTYyMywiYXV0aG9yaXRpZXMiOiJST0xFX0FETUlOIn0._fGmj76KGE9YQ2skKDZ6A2Y2_x80Hd7auszGd-oYyjPJWQ-DOvg1u6iJYGDvLdvvw7uY1lV32YCeq8RJglSePQ",
    "mesg": "User authentication success!!!"
}
```

  
**For Protected routes select Authorization as Bearer and paste token which you got from signIn request in postman**  

  
**NOTE:- If the token is missing , or token is wrong then we got 401 Error means User not authorized**  

  
**(iii) Get all Events(Both admin and user can access)**  
Get https://localhost:7070/api/events  

Request :  
```
Only Give Bearer Token
```
Response :  
```
[
    {
        "id": 1,
        "name": "abc",
        "location": "Mumbai",
        "description": "Clubing",
        "eventDate": "2025-08-27",
        "capacity": 100
    },
    {
        "id": 2,
        "name": "abcd",
        "location": "Pune",
        "description": "Conference",
        "eventDate": "2025-08-28",
        "capacity": 200
    }
]
```
**(iv) Get particular Event(Both admin and user can access) by its ID**  
Get https://localhost:7070/api/events/1  

Request :  
```
Only Give Bearer Token
```
Response :  
```

    {
        "id": 1,
        "name": "abc",
        "location": "Mumbai",
        "description": "Clubing",
        "eventDate": "2025-08-27",
        "capacity": 100
    }
```
**NOTE :- I have created project in such a way that only admin can create , edit and delete events.**  

**NOTE :- If any other user whose role is not admin , trying to do create , edit and delete events then got 403 Forbidden Error, It means User is Authenticated , but it not authorized for that particular action**  

**(v) Create Event(Only admin can create)**  
Post https://localhost:7070/api/events  

Request :  
```
Give Bearer Token in authorization +
 {
        "name": "Thinkifi Annual Party",
        "location": "Banglore",
        "description": "Annual Party",
        "eventDate": "2025-12-31",
        "capacity": 50
    }
```
Response :  
```

    {
        "id": 3,
        "name": "Thinkifi Annual Party",
        "location": "Banglore",
        "description": "Annual Party",
        "eventDate": "2025-12-31",
        "capacity": 50
    }
```
**NOTE :- Say if other user trying to access it , we get response like this**  
Response :  
```
    {
    "timestamp": "2025-08-28T12:43:01.433+00:00",
    "status": 403,
    "error": "Forbidden",
    "message": "Access Denied",
    "path": "/api/events"
}
```

**(vi) Edit Event(Only admin can edit)**  
PUT https://localhost:7070/api/events/3  

Request :  
```
Give Bearer Token in authorization +
 {
        "name": "Thinkifi Annual Party2",
        "location": "Pune",
        "description": "Annual Party new desc",
        "eventDate": "2025-12-25",
        "capacity": 100
    }
```
Response :  
```

    {
        "id": 3,
        "name": "Thinkifi Annual Party2",
        "location": "Pune",
        "description": "Annual Party new desc",
        "eventDate": "2025-12-25",
        "capacity": 100
    }
```
**(vii) Delete Event(Only admin can delete)**  
DELETE https://localhost:7070/api/events/3  

Request :  
```
Only Bearer Token in authorization
```
Response :  
```
Deleted
```

