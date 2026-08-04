package com.dinsaren.springbootjwtapi.models.req;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TeacherReq {

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String password;

    private String gender;

    private LocalDate dateOfBirth;

    private String address;

    private String photo;

}