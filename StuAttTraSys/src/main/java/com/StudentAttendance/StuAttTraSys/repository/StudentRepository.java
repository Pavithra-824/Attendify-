package com.StudentAttendance.StuAttTraSys.repository;

import com.StudentAttendance.StuAttTraSys.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByRoll(String roll);
}
