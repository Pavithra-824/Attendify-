package com.StudentAttendance.StuAttTraSys.Service;
import com.StudentAttendance.StuAttTraSys.exception.ResourceNotFoundException;
import com.StudentAttendance.StuAttTraSys.model.Student;
import com.StudentAttendance.StuAttTraSys.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {


        // Here, we ask Spring to give us the repository we created earlier.
        // This is Dependency Injection!
        @Autowired

        private StudentRepository studentRepository;

        // This method contains the logic for creating a new student.
        // For now, it just tells the repository to save the student.
        public Student createStudent(Student student) {

                return studentRepository.save(student);
        }

        public List<Student> getAllStudents() {
                // This uses the built-in findAll() method from JpaRepository
                return studentRepository.findAll();
        }

        public Optional<Student> getStudentById(Long id) {
                return studentRepository.findById(id);
        }
        // Add this inside the StudentService class

        // You will need to add: import com.attendance.tracker.exception.ResourceNotFoundException;
// Replace the old updateStudent method with this one

        public Student updateStudent(Long id, Student studentDetails) {
                // First, find the existing student. If not found, throw an exception.
                Student student = studentRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

                // If the student was found, update their fields
                student.setFirstName(studentDetails.getFirstName());
                student.setLastName(studentDetails.getLastName());

                // Save the updated student back to the database
                return studentRepository.save(student);
        }
        // Add this inside the StudentService class

        public void deleteStudent(Long id) {
                // First, check if a student with this ID exists to avoid errors
                Student student = studentRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

                // If the student exists, delete them
                studentRepository.delete(student);
        }
}
