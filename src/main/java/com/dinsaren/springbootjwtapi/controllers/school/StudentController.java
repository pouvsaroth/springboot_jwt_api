package com.dinsaren.springbootjwtapi.controllers.school;

import com.dinsaren.springbootjwtapi.models.req.StudentReq;
import com.dinsaren.springbootjwtapi.models.res.StudentRes;
import com.dinsaren.springbootjwtapi.services.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<StudentRes> create(@Valid @RequestBody StudentReq request) {
        System.out.println("===== Student API Called =====");
        StudentRes response = studentService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}