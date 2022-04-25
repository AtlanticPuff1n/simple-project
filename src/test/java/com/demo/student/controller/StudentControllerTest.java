package com.demo.student.controller;

import com.demo.student.model.Student;
import com.demo.student.model.StudentDTO;
import com.demo.student.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration(classes = {StudentController.class})
@ExtendWith(SpringExtension.class)
@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentController studentController;

    @MockBean
    private StudentService studentService;

    @Test
    void testGetStudent_Found() throws Exception {
        Student student = Student.builder()
                .id(1L)
                .firstName("firstName")
                .lastName("lastName")
                .email("email@email.com")
                .createdDate(LocalDate.parse("2022-12-31"))
                .build();
        when(this.studentService.getStudent((Long) any())).thenReturn(new ResponseEntity<>(student, HttpStatus.OK));

        mockMvc.perform(get("/student/{id}", 1L))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.firstName", Matchers.is("firstName")))
                .andExpect(jsonPath("$.lastName", Matchers.is("lastName")))
                .andExpect(jsonPath("$.email", Matchers.is("email@email.com")));
        verify(this.studentService).getStudent((Long) any());
    }

    @Test
    void testGetStudent_NotFound() throws Exception {
        when(this.studentService.getStudent((Long) any())).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        mockMvc.perform(get("/student/{id}", 1L))
                .andExpect(status().isNotFound());
        verify(this.studentService).getStudent((Long) any());
    }

    @Test
    void testGetStudents() throws Exception {
        List<Student> students = List.of(Student.builder()
                .id(1L)
                .firstName("firstName")
                .lastName("lastName")
                .email("email@email.com")
                .createdDate(LocalDate.parse("2022-12-31"))
                .build());
        when(this.studentService.getStudents()).thenReturn(new ResponseEntity<List<Student>>(students, HttpStatus.OK));

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", Matchers.hasSize(1)));
        verify(this.studentService).getStudents();
    }

    @Test
    void testDeleteStudent() throws Exception {
        when(this.studentService.deleteStudentById((Long) any())).thenReturn(new ResponseEntity<Long>(1L, HttpStatus.OK));
        mockMvc.perform(delete("/student/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
        verify(this.studentService).deleteStudentById((Long) any());
    }

    @Test
    void testCreateStudent() throws Exception {
        Student student = Student.builder()
                .id(1L)
                .firstName("firstName")
                .lastName("lastName")
                .email("email@email.com")
                .createdDate(LocalDate.parse("2022-12-31"))
                .build();
        when(this.studentService.createStudent((StudentDTO) any())).thenReturn(new ResponseEntity<>(student, HttpStatus.CREATED));

        StudentDTO studentDTO = StudentDTO.builder()
                .id(1L)
                .firstName("firstName")
                .lastName("lastName")
                .email("email@email.com")
                .build();
        String content = (new ObjectMapper()).writeValueAsString(studentDTO);

        mockMvc.perform(post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        verify(this.studentService).createStudent((StudentDTO) any());
    }

    @Test
    void testUpdateStudent() throws Exception {
        Student student = Student.builder()
                .id(1L)
                .firstName("updatedFirstName")
                .lastName("updatedLastName")
                .email("updatedEmail@email.com")
                .createdDate(LocalDate.now())
                .build();
        when(this.studentService.updateStudent((Long) any(), (StudentDTO) any()))
                .thenReturn(new ResponseEntity<Student>(student, HttpStatus.OK));

        StudentDTO studentDTO = StudentDTO.builder()
                .firstName("updatedFirstName")
                .lastName("updatedLastName")
                .email("updatedEmail@email.com")
                .build();
        String content = (new ObjectMapper()).writeValueAsString(studentDTO);

        mockMvc.perform(put("/student/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        verify(this.studentService).updateStudent((Long) any(), (StudentDTO) any());
    }
}

