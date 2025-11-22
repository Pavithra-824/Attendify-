package com.StudentAttendance.StuAttTraSys.service;

import com.StudentAttendance.StuAttTraSys.exception.ResourceNotFoundException;
import com.StudentAttendance.StuAttTraSys.model.Classroom;
import com.StudentAttendance.StuAttTraSys.model.Student;
import com.StudentAttendance.StuAttTraSys.repository.ClassroomRepository;
import com.StudentAttendance.StuAttTraSys.repository.StudentRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentService {
        private final StudentRepository studentRepository;
        private final ClassroomRepository classroomRepository;

        public StudentService(StudentRepository studentRepository, ClassroomRepository classroomRepository) {
                this.studentRepository = studentRepository;
                this.classroomRepository = classroomRepository;
        }

        public Student createStudent(Student s) {
                if (studentRepository.existsByRoll(s.getRoll())) {
                        throw new IllegalArgumentException("Roll number already exists");
                }
                if (s.getClassroom() != null && s.getClassroom().getId() != null) {
                        Long cid = s.getClassroom().getId();
                        Classroom c = classroomRepository.findById(cid)
                                .orElseThrow(() -> new ResourceNotFoundException("Class not found with id: " + cid));
                        s.setClassroom(c);
                }
                return studentRepository.save(s);
        }

        public List<Student> getAllStudents() {
                return studentRepository.findAll();
        }

        public Optional<Student> getById(Long id) {
                return studentRepository.findById(id);
        }

        public Student updateStudent(Long id, Student details) {
                Student s = studentRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
                if (!s.getRoll().equals(details.getRoll()) && studentRepository.existsByRoll(details.getRoll())) {
                        throw new IllegalArgumentException("Roll number already exists");
                }
                s.setName(details.getName());
                s.setRoll(details.getRoll());
                if (details.getClassroom() != null && details.getClassroom().getId() != null) {
                        Classroom c = classroomRepository.findById(details.getClassroom().getId())
                                .orElseThrow(() -> new ResourceNotFoundException("Class not found with id: " + details.getClassroom().getId()));
                        s.setClassroom(c);
                }
                return studentRepository.save(s);
        }

        public void deleteStudent(Long id) {
                Student s = studentRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
                studentRepository.delete(s);
        }

        // ---- Import method ----
        public ImportResult importFromXlsx(MultipartFile file, Long classId) throws Exception {
                if (file == null || file.isEmpty()) {
                        throw new IllegalArgumentException("File is empty");
                }
                Classroom classroom = classroomRepository.findById(classId)
                        .orElseThrow(() -> new ResourceNotFoundException("Class not found with id: " + classId));

                List<String> errors = new ArrayList<>();
                int inserted = 0;
                int skipped = 0;

                try (InputStream in = file.getInputStream();
                     Workbook workbook = new XSSFWorkbook(in)) {

                        Sheet sheet = workbook.getSheetAt(0);
                        if (sheet == null) throw new IllegalArgumentException("Excel sheet is empty");

                        Row headerRow = sheet.getRow(0);
                        if (headerRow == null) throw new IllegalArgumentException("Excel header row is missing");

                        int rollCol = -1, nameCol = -1;
                        for (Cell cell : headerRow) {
                                String h = cell.getStringCellValue().trim().toLowerCase();
                                if (h.equals("rollnumber") || h.equals("roll_number") || h.equals("roll")) rollCol = cell.getColumnIndex();
                                if (h.equals("name")) nameCol = cell.getColumnIndex();
                        }
                        if (rollCol == -1 || nameCol == -1) {
                                throw new IllegalArgumentException("Excel must have headers: rollNumber and name");
                        }

                        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                                Row row = sheet.getRow(r);
                                if (row == null) continue;
                                String roll = getCellString(row.getCell(rollCol)).trim();
                                String name = getCellString(row.getCell(nameCol)).trim();

                                if (roll.isEmpty() && name.isEmpty()) continue;

                                if (roll.isEmpty() || name.isEmpty()) {
                                        errors.add("Row " + (r + 1) + ": missing roll or name");
                                        skipped++;
                                        continue;
                                }

                                if (studentRepository.existsByRoll(roll)) {
                                        errors.add("Row " + (r + 1) + ": roll '" + roll + "' already exists");
                                        skipped++;
                                        continue;
                                }

                                Student s = new Student();
                                s.setRoll(roll);
                                s.setName(name);
                                s.setClassroom(classroom);
                                studentRepository.save(s);
                                inserted++;
                        }

                } catch (Exception ex) {
                        throw ex;
                }

                ImportResult res = new ImportResult();
                res.setInserted(inserted);
                res.setSkipped(skipped);
                res.setErrors(errors);
                return res;
        }

        private static String getCellString(Cell cell) {
                if (cell == null) return "";
                if (cell.getCellType() == CellType.STRING) return cell.getStringCellValue();
                if (cell.getCellType() == CellType.NUMERIC) {
                        double d = cell.getNumericCellValue();
                        long l = (long) d;
                        return String.valueOf(l);
                }
                if (cell.getCellType() == CellType.BOOLEAN) return String.valueOf(cell.getBooleanCellValue());
                return "";
        }

        public static class ImportResult {
                private int inserted;
                private int skipped;
                private List<String> errors = new ArrayList<>();
                public int getInserted() { return inserted; }
                public void setInserted(int inserted) { this.inserted = inserted; }
                public int getSkipped() { return skipped; }
                public void setSkipped(int skipped) { this.skipped = skipped; }
                public List<String> getErrors() { return errors; }
                public void setErrors(List<String> errors) { this.errors = errors; }
        }
}
