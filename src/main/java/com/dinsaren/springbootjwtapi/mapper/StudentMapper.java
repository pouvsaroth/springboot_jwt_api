package com.dinsaren.springbootjwtapi.mapper;

import com.dinsaren.springbootjwtapi.models.User;
import com.dinsaren.springbootjwtapi.models.school.Student;
import com.dinsaren.springbootjwtapi.models.res.StudentRes;

public class StudentMapper {

    private StudentMapper() {
    }

    public static StudentRes toResponse(Student student,  String baseUrl) {

        User user = student.getUser();

        StudentRes res = new StudentRes();

        // Student
        res.setId(student.getId());

        // User
        res.setUserId(user.getId());
        res.setUsername(user.getUsername());
        res.setEmail(user.getEmail());

        // Student Profile
        res.setFirstName(student.getFirstName());
        res.setLastName(student.getLastName());
        res.setGender(student.getGender());
        res.setDateOfBirth(student.getDateOfBirth());
        res.setPhone(student.getPhone());
        res.setAddress(student.getAddress());
        res.setPhoto(student.getPhoto());

        if (student.getPhoto() != null &&
                !student.getPhoto().isBlank()) {

            res.setPhoto(student.getPhoto());
        }

        res.setIsActive(student.getIsActive());

        // Audit
        res.setCreatedAt(student.getCreateAt());
        res.setUpdatedAt(student.getUpdateAt());

        return res;
    }
}