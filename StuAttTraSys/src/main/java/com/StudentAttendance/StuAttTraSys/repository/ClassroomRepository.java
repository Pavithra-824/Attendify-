package com.StudentAttendance.StuAttTraSys.repository;

import com.StudentAttendance.StuAttTraSys.model.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    Optional<Classroom> findByName(String name);
    boolean existsByName(String name);
}
