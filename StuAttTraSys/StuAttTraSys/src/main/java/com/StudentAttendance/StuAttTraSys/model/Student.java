package com.StudentAttendance.StuAttTraSys.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity // This tells Spring: "This class is a database table"
public class Student {

    @Id // This marks the 'id' field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // This makes the ID auto-generate
    private Long id;

    private String firstName;
    private String lastName;

    // Getters and Setters (required by JPA)
    // You can auto-generate these in IntelliJ: Right-click -> Generate -> Getters and Setters -> Select all fields

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}