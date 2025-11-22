package com.StudentAttendance.StuAttTraSys.controller;

import com.StudentAttendance.StuAttTraSys.model.Attendance;
import com.StudentAttendance.StuAttTraSys.service.AttendanceService;
import com.StudentAttendance.StuAttTraSys.exception.ResourceNotFoundException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.List;
import java.util.Map;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;
    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    /**
     * Mark or update attendance for a student on a date for a class.
     * Body JSON: { "studentId": 1, "classroomId": 1, "date": "2025-11-12", "status": "PRESENT" }
     */
    @PostMapping
    public ResponseEntity<?> mark(@RequestBody Map<String, String> body) {
        try {
            Long studentId = Long.valueOf(body.get("studentId"));
            Long classroomId = Long.valueOf(body.get("classroomId"));
            LocalDate date = LocalDate.parse(body.get("date"));
            Attendance.Status status = Attendance.Status.valueOf(body.get("status").toUpperCase());
            Attendance saved = attendanceService.markAttendance(studentId, classroomId, date, status);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid input: " + ex.getMessage()));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(404).body(Map.of("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(Map.of("error", ex.getMessage()));
        }
    }

    /**
     * Get attendance for a class on a specific date:
     * GET /api/attendance/class/1?date=2025-11-12
     */
    @GetMapping("/class/{classId}")
    public ResponseEntity<?> classByDate(@PathVariable Long classId,
                                         @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            List<Attendance> list = attendanceService.getClassAttendanceByDate(classId, date);
            return ResponseEntity.ok(list);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(404).body(Map.of("error", ex.getMessage()));
        }
    }

    /**
     * Student attendance history:
     * GET /api/attendance/student/1
     */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> studentHistory(@PathVariable Long studentId) {
        try {
            List<Attendance> list = attendanceService.getStudentAttendanceHistory(studentId);
            return ResponseEntity.ok(list);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(404).body(Map.of("error", ex.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Map<String, String> body) {
        try {
            Attendance.Status status = Attendance.Status.valueOf(body.get("status").toUpperCase());
            Attendance updated = attendanceService.updateAttendance(id, status);
            return ResponseEntity.ok(updated);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(404).body(Map.of("error", ex.getMessage()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid status"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            attendanceService.deleteAttendance(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(404).body(Map.of("error", ex.getMessage()));
        }
    }

    /**
     * Optional: student attendance percentage over a date range:
     * GET /api/attendance/student/1/percentage?from=2025-11-01&to=2025-11-30
     */
    @GetMapping("/student/{studentId}/percentage")
    public ResponseEntity<?> percentage(@PathVariable Long studentId,
                                        @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                        @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        try {
            double pct = attendanceService.studentAttendancePercentage(studentId, from, to);
            return ResponseEntity.ok(Map.of("percentage", pct));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(404).body(Map.of("error", ex.getMessage()));
        }
    }
}
