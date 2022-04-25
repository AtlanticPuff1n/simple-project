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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
        Student student = Student.builder()
                .id(1L)
                .firstName("firstName")
                .lastName("lastName")
                .email("email@email.com")
                .createdDate(LocalDate.parse("2022-12-31"))
                .build();

        Optional<Student> ofResult = Optional.of(student);
        when(this.studentRepository.findById((Long) any())).thenReturn(ofResult);
        ResponseEntity<Student> actualStudent = this.studentService.getStudent(1L);

        assertTrue(actualStudent.hasBody());
        assertEquals(HttpStatus.OK, actualStudent.getStatusCode());
        verify(this.studentRepository).findById((Long) any());
    }

    @Test
    void testGetStudents() {
        Student student = Student.builder()
                .id(1L)
                .firstName("firstName")
                .lastName("lastName")
                .email("email@email.com")
                .createdDate(LocalDate.parse("2022-12-31"))
                .build();

        List<Student> students = new ArrayList<>();
        students.add(student);
        students.add(student);

        when(this.studentRepository.findAll()).thenReturn(students);
        ResponseEntity<List<Student>> actualStudents = this.studentService.getStudents();

        assertTrue(actualStudents.hasBody());
        assertEquals(2, actualStudents.getBody().size());
        assertEquals(HttpStatus.OK, actualStudents.getStatusCode());
        verify(this.studentRepository).findAll();
    }

    @Test
    void testDeleteStudent() {
        doNothing().when(this.studentRepository).deleteById((Long) any());
        ResponseEntity<Long> actualDeleteStudentResult = this.studentService.deleteStudentById(1L);

        assertEquals(1L, actualDeleteStudentResult.getBody().longValue());
        assertEquals(HttpStatus.OK, actualDeleteStudentResult.getStatusCode());
        verify(this.studentRepository).deleteById((Long) any());
    }

    @Test
    void testCreateStudent() {
        Student student = Student.builder()
                .id(1L)
                .firstName("firstName")
                .lastName("lastName")
                .email("email@email.com")
                .createdDate(LocalDate.parse("2022-12-31"))
                .build();
        when(this.studentRepository.save((Student) any())).thenReturn(student);

        Student student2 = Student.builder()
                .id(1L)
                .firstName("firstName")
                .lastName("lastName")
                .email("email@email.com")
                .createdDate(LocalDate.parse("2022-12-31"))
                .build();
        when(this.studentMapper.toStudent((StudentDTO) any())).thenReturn(student2);
        ResponseEntity<Student> actualCreateStudentResult = this.studentService.createStudent(new StudentDTO());

        assertTrue(actualCreateStudentResult.hasBody());
        assertEquals(HttpStatus.CREATED, actualCreateStudentResult.getStatusCode());
        verify(this.studentRepository).save((Student) any());
        verify(this.studentMapper).toStudent((StudentDTO) any());
    }

    @Test
    void testUpdateStudent_Updated() {
        Student student = Student.builder()
                .id(1L)
                .firstName("firstName")
                .lastName("lastName")
                .email("email@email.com")
                .createdDate(LocalDate.parse("2022-12-31"))
                .build();
        Optional<Student> ofResult = Optional.of(student);

        Student updatedStudent = Student.builder()
                .id(2L)
                .firstName("updatedFirstName")
                .lastName("updatedLastName")
                .email("updatedEmail@email.com")
                .createdDate(LocalDate.parse("2022-12-31"))
                .build();
        when(this.studentRepository.save((Student) any())).thenReturn(updatedStudent);
        when(this.studentRepository.findById((Long) any())).thenReturn(ofResult);

        ResponseEntity<Student> actualUpdateStudentResult = this.studentService.updateStudent(1L, new StudentDTO());

        assertTrue(actualUpdateStudentResult.hasBody());
        assertEquals(HttpStatus.CREATED, actualUpdateStudentResult.getStatusCode());
        verify(this.studentRepository).save((Student) any());
        verify(this.studentRepository).findById((Long) any());
    }

    @Test
    void testUpdateStudent_NotFound() {
        Student student = Student.builder()
                .id(1L)
                .firstName("firstName")
                .lastName("lastName")
                .email("email@email.com")
                .createdDate(LocalDate.parse("2022-12-31"))
                .build();
        when(this.studentRepository.save((Student) any())).thenReturn(student);
        when(this.studentRepository.findById((Long) any())).thenReturn(Optional.empty());

        ResponseEntity<Student> actualUpdateStudentResult = this.studentService.updateStudent(2L, new StudentDTO());

        assertNull(actualUpdateStudentResult.getBody());
        assertEquals(HttpStatus.NOT_FOUND, actualUpdateStudentResult.getStatusCode());
        verify(this.studentRepository).findById((Long) any());
    }

}

