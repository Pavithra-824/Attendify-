# Attendify - Student Attendance System (Backend)
https://github.com/user-attachments/assets/183ca9ec-25d3-4655-bc35-6f8a5b39d05d

## 📋 Overview
This is the **Backend API** for Attendify, a student attendance management system. It provides a robust REST API for managing classrooms, students, and attendance records. It is built with **Spring Boot** and uses **MySQL** for permanent data storage.

## 🚀 Tech Stack
* **Language:** Java 17
* **Framework:** Spring Boot 3.2.0
* **Database:** MySQL 8.0 (Spring Data JPA / Hibernate)
* **Build Tool:** Maven
* **Excel Processing:** Apache POI (for bulk student import)

## ✨ Key Features
* **Classroom Management:** Create, update, and delete classrooms.
* **Student Management:**
    * CRUD operations for students.
    * **Bulk Import:** Upload `.xlsx` files to add multiple students at once.
* **Attendance Logic:**
    * Mark students as **Present** or **Absent** for specific dates.
    * Prevent duplicate entries for the same day.
* **Reporting:** Calculate attendance percentage for students over custom date ranges.
* **CORS Enabled:** Configured to communicate with the React frontend.

## 🛠️ Setup & Installation

### Prerequisites
* Java JDK 17+
* MySQL Server installed and running

### 1. Configure Database
1. Open **MySQL Workbench**.
2. Create a new schema (database) named: `students_attendance`
3. Open `src/main/resources/application.properties` and update your MySQL credentials:
   ```properties
   spring.datasource.username=root
   spring.datasource.password=YOUR_MYSQL_PASSWORD

2. Run the Application
Open a terminal in the project root and run:

Using Maven Wrapper (Windows):

PowerShell

.\mvnw spring-boot:run
Using Maven Wrapper (Mac/Linux):

Bash

./mvnw spring-boot:run
The server will start at http://localhost:8080.

🔌 API Endpoints
GET /api/classrooms - List all classes

POST /api/classrooms - Create a new class

POST /api/students - Add a single student

POST /api/students/import - Bulk upload students from Excel

POST /api/attendance - Mark attendance
