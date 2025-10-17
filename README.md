# Attendify - Student Attendance Tracker API

A robust RESTful API built with Java and Spring Boot to manage and streamline student attendance records. This project serves as my final-year project, demonstrating key backend development skills in building scalable and maintainable applications.

---

## 🎯 Key Features

-   **Student Management:** Full CRUD (Create, Read, Update, Delete) operations for student data.
-   **Course Management:** Endpoints to manage course information and enrollment.
-   **Attendance Tracking:** API to mark daily attendance for students in specific courses.
-   **Reporting:** Generate attendance reports by student, course, or a specific date range.

---

## 🛠️ Technologies Used

-   **Language:** Java
-   **Framework:** Spring Boot
-   **Data Persistence:** Spring Data JPA / Hibernate
-   **Database:** MySQL
-   **Build Tool:** Maven
-   **API Testing:** Postman

---

## 🚀 How to Run Locally

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/your-username/Attendify.git](https://github.com/your-username/Attendify.git)
    ```
2.  **Configure the database:**
    -   Open the `src/main/resources/application.properties` file.
    -   Update the `spring.datasource.url`, `spring.datasource.username`, and `spring.datasource.password` to match your local MySQL setup.
3.  **Build and run the application:**
    -   Build the project using Maven: `mvn clean install`
    -   Run the main application file to start the server.
