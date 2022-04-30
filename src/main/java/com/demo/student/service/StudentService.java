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

import static com.demo.student.constants.Constants.STUDENT_NOT_FOUND_ERROR_CODE;
import static com.demo.student.constants.Constants.STUDENT_NOT_FOUND_ERROR_MESSAGE;

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
                                        .errorCode(STUDENT_NOT_FOUND_ERROR_CODE)
                                        .errorMessage(STUDENT_NOT_FOUND_ERROR_MESSAGE + id)
                                        .referenceId(UUID.randomUUID().toString())
                                        .build()
                        ));
    }

    public ResponseEntity<List<Student>> getStudents() {
        return ResponseEntity.ok(studentRepository.findAll());
    }

    public ResponseEntity<Object> deleteStudentById(Long id) {
        return studentRepository.findById(id)
                .<ResponseEntity<Object>>map(student -> {
                    studentRepository.delete(student);
                    return ResponseEntity.ok(id);
                }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(
                                ErrorResponse.builder()
                                        .errorCode(STUDENT_NOT_FOUND_ERROR_CODE)
                                        .errorMessage(STUDENT_NOT_FOUND_ERROR_MESSAGE + id)
                                        .referenceId(UUID.randomUUID().toString())
                                        .build()
                        ));
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
                            .status(HttpStatus.OK)
                            .body(studentRepository.save(student));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(
                                ErrorResponse.builder()
                                        .errorCode(STUDENT_NOT_FOUND_ERROR_CODE)
                                        .errorMessage(STUDENT_NOT_FOUND_ERROR_MESSAGE + id)
                                        .referenceId(UUID.randomUUID().toString())
                                        .build()
                        ));
    }

}
