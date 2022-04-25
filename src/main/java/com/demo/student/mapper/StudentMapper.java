package com.demo.student.mapper;

import com.demo.student.model.Student;
import com.demo.student.model.StudentDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    Student toStudent(StudentDTO studentDTO);
}
