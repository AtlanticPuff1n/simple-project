package com.demo.student.service;

import com.demo.student.mapper.StudentMapper;
import com.demo.student.model.Student;
import com.demo.student.model.StudentDTO;
import com.demo.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    public ResponseEntity<Student> getStudent(Long id) {
        return studentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    public ResponseEntity<List<Student>> getStudents() {
        return ResponseEntity.ok(studentRepository.findAll());
    }

    public ResponseEntity<Long> deleteStudentById(Long id) {
        studentRepository.deleteById(id);
        return ResponseEntity.ok(id);
    }

    public ResponseEntity<Student> createStudent(StudentDTO studentDTO) {
        Student student = studentMapper.toStudent(studentDTO);
        student.setCreatedDate(LocalDate.now());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(studentRepository.save(student));
    }

    public ResponseEntity<Student> updateStudent(Long id, StudentDTO studentDTO) {
        return studentRepository.findById(id)
                .map(student -> {
                    student.setFirstName(studentDTO.getFirstName());
                    student.setLastName(studentDTO.getLastName());
                    student.setEmail(studentDTO.getEmail());
                    student.setCreatedDate(LocalDate.now());
                    return ResponseEntity
                            .status(HttpStatus.CREATED)
                            .body(studentRepository.save(student));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
