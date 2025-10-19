package com.StudentAttendance.StuAttTraSys.controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;
import java.util.Optional;

import com.StudentAttendance.StuAttTraSys.Service.StudentService;
import com.StudentAttendance.StuAttTraSys.model.Student;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class StudentController {

    // The controller now has access to the service layer
    @Autowired
    private StudentService studentService;

    // This method handles POST requests to create a new student
    @PostMapping("/students")
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }
    @GetMapping("/students")
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/students/{id}")
    public Optional<Student> getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }
    // Add this inside the StudentController class
// You'll need to add: import org.springframework.web.bind.annotation.PutMapping;

    @PutMapping("/students/{id}")
    public Student updateStudent(@PathVariable Long id, @RequestBody Student studentDetails) {
        return studentService.updateStudent(id, studentDetails);
    }
    // Add this inside the StudentController class
// You'll need to add: import org.springframework.web.bind.annotation.DeleteMapping;

    @DeleteMapping("/students/{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }

}