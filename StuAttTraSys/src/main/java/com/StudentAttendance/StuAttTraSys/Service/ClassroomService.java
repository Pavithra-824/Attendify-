package com.StudentAttendance.StuAttTraSys.service;

import com.StudentAttendance.StuAttTraSys.exception.ResourceNotFoundException;
import com.StudentAttendance.StuAttTraSys.model.Classroom;
import com.StudentAttendance.StuAttTraSys.repository.ClassroomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ClassroomService {
    private final ClassroomRepository classroomRepository;
    public ClassroomService(ClassroomRepository classroomRepository) {
        this.classroomRepository = classroomRepository;
    }

    public Classroom createClassroom(Classroom classroom) {
        if (classroomRepository.existsByName(classroom.getName())) {
            throw new IllegalArgumentException("Classroom with that name already exists");
        }
        return classroomRepository.save(classroom);
    }

    public Classroom updateClassroom(Long id, Classroom details) {
        Classroom c = classroomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found with id: " + id));
        if (!c.getName().equals(details.getName()) && classroomRepository.existsByName(details.getName())) {
            throw new IllegalArgumentException("Classroom with that name already exists");
        }
        c.setName(details.getName());
        c.setYear(details.getYear());
        c.setSection(details.getSection());
        return classroomRepository.save(c);
    }

    public void deleteClassroom(Long id) {
        Classroom c = classroomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found with id: " + id));
        classroomRepository.delete(c); // cascade remove students
    }

    public Classroom getById(Long id) {
        return classroomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Classroom not found with id: " + id));
    }

    public List<Classroom> listAll() {
        return classroomRepository.findAll();
    }
}
