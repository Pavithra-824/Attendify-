package com.StudentAttendance.StuAttTraSys.repository;

import com.StudentAttendance.StuAttTraSys.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByClassroomIdAndDate(Long classroomId, LocalDate date);
    List<Attendance> findByStudentIdOrderByDateDesc(Long studentId);
    Optional<Attendance> findByStudentIdAndDateAndClassroomId(Long studentId, LocalDate date, Long classroomId);
    List<Attendance> findByClassroomIdAndDateBetween(Long classId, LocalDate from, LocalDate to);
}
