package com.dinsaren.springbootjwtapi.models.res;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class TeacherRes {

    private Integer id;

    private Integer userId;

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private String gender;

    private LocalDate dateOfBirth;

    private String phone;

    private String address;

    private String photo;

    private Boolean isActive;
    // Audit Information
    private Date createdAt;
    private Date updatedAt;
}