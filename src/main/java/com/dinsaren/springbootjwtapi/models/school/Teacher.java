package com.dinsaren.springbootjwtapi.models.school;
import com.dinsaren.springbootjwtapi.models.BaseEntity;

import com.dinsaren.springbootjwtapi.models.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Entity
@Table(name = "teachers")
@Data
@EqualsAndHashCode(callSuper = false)
public class Teacher extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    private String phone;

    private String address;

    private String photo;

    @Column(name = "is_active")
    private Boolean isActive;
}