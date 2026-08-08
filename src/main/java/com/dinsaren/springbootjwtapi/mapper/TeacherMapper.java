package com.dinsaren.springbootjwtapi.mapper;

import com.dinsaren.springbootjwtapi.models.User;
import com.dinsaren.springbootjwtapi.models.school.Student;
import com.dinsaren.springbootjwtapi.models.res.TeacherRes;
import com.dinsaren.springbootjwtapi.models.school.Teacher;

public class TeacherMapper {

    private TeacherMapper() {
    }

    public static TeacherRes toResponse(Teacher teacher) {

        User user = teacher.getUser();

        TeacherRes res = new TeacherRes();

        // Student
        res.setId(teacher.getId());

        // User
        res.setUserId(user.getId());
        res.setUsername(user.getUsername());
        res.setEmail(user.getEmail());

        // Student Profile
        res.setFirstName(teacher.getFirstName());
        res.setLastName(teacher.getLastName());
        res.setGender(teacher.getGender());
        res.setDateOfBirth(teacher.getDateOfBirth());
        res.setPhone(teacher.getPhone());
        res.setAddress(teacher.getAddress());
        res.setPhoto(teacher.getPhoto());
        if (teacher.getPhoto() != null &&
                !teacher.getPhoto().isBlank()) {

            res.setPhoto(teacher.getPhoto());
        }
        res.setIsActive(teacher.getIsActive());

        // Audit
        res.setCreatedAt(teacher.getCreateAt());
        res.setUpdatedAt(teacher.getUpdateAt());

        return res;
    }
}