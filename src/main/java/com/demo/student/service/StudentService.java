package com.demo.student.service;

import com.demo.student.exception.ErrorResponse;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    public ResponseEntity<Object> getStudent(Long id) {
        return studentRepository.findById(id)
                .<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(
                                ErrorResponse.builder()
                                        .errorCode("STUDENT_NOT_FOUND")
                                        .errorMessage("Student does not exist with id:" + id)
                                        .referenceId(UUID.randomUUID().toString())
                                        .build()
                        ));
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

    public ResponseEntity<Object> updateStudent(Long id, StudentDTO studentDTO) {
        return studentRepository.findById(id)
                .<ResponseEntity<Object>>map(student -> {
                    student.setFirstName(studentDTO.getFirstName());
                    student.setLastName(studentDTO.getLastName());
                    student.setEmail(studentDTO.getEmail());
                    student.setCreatedDate(LocalDate.now());
                    return ResponseEntity
                            .status(HttpStatus.CREATED)
                            .body(studentRepository.save(student));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(
                                ErrorResponse.builder()
                                        .errorCode("STUDENT_NOT_FOUND")
                                        .errorMessage("Student does not exist with id:" + id)
                                        .referenceId(UUID.randomUUID().toString())
                                        .build()
                        ));
    }

}
