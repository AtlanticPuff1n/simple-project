package com.demo.student.service;

import com.demo.student.mapper.StudentMapper;
import com.demo.student.model.Student;
import com.demo.student.model.StudentDTO;
import com.demo.student.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {StudentService.class})
@ExtendWith(SpringExtension.class)
class StudentServiceTest {
    @MockBean
    private StudentMapper studentMapper;

    @MockBean
    private StudentRepository studentRepository;

    @Autowired
    private StudentService studentService;

    @Test
    void testGetStudent() {
        Optional<Student> ofResult = Optional.of(new Student());
        when(this.studentRepository.findById(any())).thenReturn(ofResult);
        ResponseEntity<Student> actualStudent = this.studentService.getStudent(1L);

        assertTrue(actualStudent.hasBody());
        assertEquals(HttpStatus.OK, actualStudent.getStatusCode());
        verify(this.studentRepository).findById(any());
    }

    @Test
    void testGetStudents() {
        List<Student> students = new ArrayList<>();
        students.add(new Student());
        students.add(new Student());

        when(this.studentRepository.findAll()).thenReturn(students);
        ResponseEntity<List<Student>> actualStudents = this.studentService.getStudents();

        assertTrue(actualStudents.hasBody());
        assertEquals(2, Objects.requireNonNull(actualStudents.getBody()).size());
        assertEquals(HttpStatus.OK, actualStudents.getStatusCode());
        verify(this.studentRepository).findAll();
    }

    @Test
    void testDeleteStudent() {
        doNothing().when(this.studentRepository).deleteById(any());
        ResponseEntity<Long> actualDeleteStudentResult = this.studentService.deleteStudentById(1L);

        assertEquals(1L, Objects.requireNonNull(actualDeleteStudentResult.getBody()).longValue());
        assertEquals(HttpStatus.OK, actualDeleteStudentResult.getStatusCode());
        verify(this.studentRepository).deleteById(any());
    }

    @Test
    void testCreateStudent() {
        when(this.studentRepository.save(any())).thenReturn(new Student());
        when(this.studentMapper.toStudent(any())).thenReturn(new Student());
        ResponseEntity<Student> actualCreateStudentResult = this.studentService.createStudent(new StudentDTO());

        assertTrue(actualCreateStudentResult.hasBody());
        assertEquals(HttpStatus.CREATED, actualCreateStudentResult.getStatusCode());
        verify(this.studentRepository).save(any());
        verify(this.studentMapper).toStudent(any());
    }

    @Test
    void testUpdateStudent_Updated() {
        Optional<Student> ofResult = Optional.of(new Student());
        when(this.studentRepository.save(any())).thenReturn(new Student());
        when(this.studentRepository.findById(any())).thenReturn(ofResult);

        ResponseEntity<Student> actualUpdateStudentResult = this.studentService.updateStudent(1L, new StudentDTO());

        assertTrue(actualUpdateStudentResult.hasBody());
        assertEquals(HttpStatus.CREATED, actualUpdateStudentResult.getStatusCode());
        verify(this.studentRepository).save(any());
        verify(this.studentRepository).findById(any());
    }

    @Test
    void testUpdateStudent_NotFound() {
        when(this.studentRepository.save(any())).thenReturn(new Student());
        when(this.studentRepository.findById(any())).thenReturn(Optional.empty());

        ResponseEntity<Student> actualUpdateStudentResult = this.studentService.updateStudent(1L, new StudentDTO());

        assertNull(actualUpdateStudentResult.getBody());
        assertEquals(HttpStatus.NOT_FOUND, actualUpdateStudentResult.getStatusCode());
        verify(this.studentRepository).findById(any());
    }

}

