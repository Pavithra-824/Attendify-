package com.StudentAttendance.StuAttTraSys.service;

import com.StudentAttendance.StuAttTraSys.exception.ResourceNotFoundException;
import com.StudentAttendance.StuAttTraSys.model.Attendance;
import com.StudentAttendance.StuAttTraSys.model.Classroom;
import com.StudentAttendance.StuAttTraSys.model.Student;
import com.StudentAttendance.StuAttTraSys.repository.AttendanceRepository;
import com.StudentAttendance.StuAttTraSys.repository.ClassroomRepository;
import com.StudentAttendance.StuAttTraSys.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final ClassroomRepository classroomRepository;

    public AttendanceService(AttendanceRepository attendanceRepository,
                             StudentRepository studentRepository,
                             ClassroomRepository classroomRepository) {
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
        this.classroomRepository = classroomRepository;
    }

    /**
     * Mark attendance. If a record exists for student+date+classroom, update it.
     * Otherwise create new.
     */
    public Attendance markAttendance(Long studentId, Long classroomId, LocalDate date, Attendance.Status status) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found with id: " + classroomId));

        Optional<Attendance> existing = attendanceRepository.findByStudentIdAndDateAndClassroomId(studentId, date, classroomId);
        Attendance att;
        if (existing.isPresent()) {
            att = existing.get();
            att.setStatus(status);
        } else {
            att = new Attendance();
            att.setStudent(student);
            att.setClassroom(classroom);
            att.setDate(date);
            att.setStatus(status);
        }

        return attendanceRepository.save(att);
    }

    public List<Attendance> getClassAttendanceByDate(Long classId, LocalDate date) {
        // ensure class exists
        classroomRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found with id: " + classId));
        return attendanceRepository.findByClassroomIdAndDate(classId, date);
    }

    public List<Attendance> getStudentAttendanceHistory(Long studentId) {
        // ensure student exists
        studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));
        return attendanceRepository.findByStudentIdOrderByDateDesc(studentId);
    }

    public void deleteAttendance(Long id) {
        Attendance a = attendanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance not found with id: " + id));
        attendanceRepository.delete(a);
    }

    public Attendance updateAttendance(Long id, Attendance.Status status) {
        Attendance a = attendanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance not found with id: " + id));
        a.setStatus(status);
        return attendanceRepository.save(a);
    }

    /**
     * Optional helper: attendance percentage for a student in a date range.
     */
    public double studentAttendancePercentage(Long studentId, LocalDate from, LocalDate to) {
        List<Attendance> list = attendanceRepository.findByStudentIdOrderByDateDesc(studentId)
                .stream()
                .filter(a -> !a.getDate().isBefore(from) && !a.getDate().isAfter(to))
                .toList();
        if (list.isEmpty()) return 0.0;
        long present = list.stream().filter(a -> a.getStatus() == Attendance.Status.PRESENT).count();
        return (present * 100.0) / list.size();
    }
}
