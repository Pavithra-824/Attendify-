package com.StudentAttendance.StuAttTraSys.model;

import jakarta.persistence.*;

@Entity
@Table(name = "student", indexes = {@Index(columnList = "roll", name = "idx_roll")})
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String roll;   // unique across system

    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;

    private String status;

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRoll() { return roll; }
    public void setRoll(String roll) { this.roll = roll; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Classroom getClassroom() { return classroom; }
    public void setClassroom(Classroom classroom) { this.classroom = classroom; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
