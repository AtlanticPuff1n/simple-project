package com.demo.student.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetStudent_Found() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/student/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("id", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("firstName", Matchers.is("firstName2")))
                .andExpect(MockMvcResultMatchers.jsonPath("lastName", Matchers.is("lastName2")))
                .andExpect(status().isOk());
    }

    @Test
    void testGetStudent_NotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/student/0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetStudents_Found() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/students")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", Matchers.hasSize(3)));
    }

    @Test
    void testDeleteStudent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/student/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteStudent_NotFound() throws Exception {
        int studentId = 0;
        mockMvc.perform(MockMvcRequestBuilders.delete("/student/" + studentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("errorCode", Matchers.is("STUDENT_NOT_FOUND")))
                .andExpect(MockMvcResultMatchers.jsonPath("errorMessage", Matchers.is("Student does not exist with id: " + studentId)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateStudent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"firstName\":\"createdFirstName\"," +
                                "\"lastName\":\"createdLastName\"," +
                                "\"email\":\"createdEmail@email.com\"" +
                                "}"))
                .andExpect(MockMvcResultMatchers.jsonPath("firstName", Matchers.is("createdFirstName")))
                .andExpect(MockMvcResultMatchers.jsonPath("lastName", Matchers.is("createdLastName")))
                .andExpect(MockMvcResultMatchers.jsonPath("email", Matchers.is("createdEmail@email.com")))
                .andExpect(status().isCreated());
    }

    @Test
    void testUpdateStudent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/student/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"firstName\":\"updatedFirstName\"," +
                                "\"lastName\":\"updatedLastName\"," +
                                "\"email\":\"updatedEmail@email.com\"" +
                                "}"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("firstName", Matchers.is("updatedFirstName")))
                .andExpect(MockMvcResultMatchers.jsonPath("lastName", Matchers.is("updatedLastName")))
                .andExpect(MockMvcResultMatchers.jsonPath("email", Matchers.is("updatedEmail@email.com")))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateStudent_NotFound() throws Exception {
        int studentId = 0;
        mockMvc.perform(MockMvcRequestBuilders.put("/student/" + studentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"firstName\":\"updatedFirstName\"," +
                                "\"lastName\":\"updatedLastName\"," +
                                "\"email\":\"updatedEmail@email.com\"" +
                                "}"))
                .andExpect(MockMvcResultMatchers.jsonPath("errorCode", Matchers.is("STUDENT_NOT_FOUND")))
                .andExpect(MockMvcResultMatchers.jsonPath("errorMessage", Matchers.is("Student does not exist with id: " + studentId)))
                .andExpect(status().isNotFound());
    }
}