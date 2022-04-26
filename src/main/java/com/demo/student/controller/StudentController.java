package com.demo.student.controller;

import com.demo.student.model.Student;
import com.demo.student.model.StudentDTO;
import com.demo.student.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/student/{id}")
    public ResponseEntity<Object> getStudent(@PathVariable Long id) {
        return studentService.getStudent(id);
    }

    @GetMapping("/students")
    public ResponseEntity<List<Student>> getStudents() {
        return studentService.getStudents();
    }

    @DeleteMapping("/student/{id}")
    public ResponseEntity<Object> deleteStudent(@PathVariable Long id) {
        return studentService.deleteStudentById(id);
    }

    @PostMapping("/student")
    public ResponseEntity<Student> createStudent(@RequestBody StudentDTO studentDTO) {
        return studentService.createStudent(studentDTO);
    }

    @PutMapping("/student/{id}")
    public ResponseEntity<Object> updateStudent(@PathVariable Long id, @RequestBody StudentDTO studentDTO) {
        return studentService.updateStudent(id, studentDTO);
    }

}
