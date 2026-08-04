package com.dinsaren.springbootjwtapi.models.req;
import lombok.Data;
import java.time.LocalDate;

@Data
public class StudentReq
{
    // User Information
    private String email;
    private String password;

    // Student Information
    private String firstName;
    private String lastName;
    private String gender;
    private LocalDate dateOfBirth;
    private String phone;
    private String address;
    private String photo;
}
