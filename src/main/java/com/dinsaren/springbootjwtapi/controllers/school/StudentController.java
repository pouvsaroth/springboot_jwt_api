package com.dinsaren.springbootjwtapi.controllers.school;

import com.dinsaren.springbootjwtapi.models.req.StudentReq;
import com.dinsaren.springbootjwtapi.models.res.StudentRes;
import com.dinsaren.springbootjwtapi.services.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<StudentRes> create(@Valid @RequestBody StudentReq request) {
        StudentRes response = studentService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentRes> update(
            @PathVariable Integer id,
            @RequestBody StudentReq request) {

        return ResponseEntity.ok(studentService.update(id, request));
    }

    @GetMapping
    public ResponseEntity<List<StudentRes>> getAll() {

        return ResponseEntity.ok(studentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentRes> getById(@PathVariable Integer id) {

        return ResponseEntity.ok(studentService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {

        studentService.delete(id);

        return ResponseEntity.noContent().build();
    }
}