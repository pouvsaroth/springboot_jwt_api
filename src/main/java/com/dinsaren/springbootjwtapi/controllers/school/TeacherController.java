package com.dinsaren.springbootjwtapi.controllers.school;

import com.dinsaren.springbootjwtapi.models.req.TeacherReq;
import com.dinsaren.springbootjwtapi.models.res.TeacherRes;
import com.dinsaren.springbootjwtapi.services.TeacherService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @PostMapping
    public ResponseEntity<TeacherRes> create(@Valid @RequestBody TeacherReq request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(teacherService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeacherRes> update(
            @PathVariable Integer id,
            @RequestBody TeacherReq request) {

        return ResponseEntity.ok(teacherService.update(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherRes> findById(@PathVariable Integer id) {

        return ResponseEntity.ok(teacherService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<TeacherRes>> findAll() {

        return ResponseEntity.ok(teacherService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {

        teacherService.delete(id);

        return ResponseEntity.noContent().build();
    }
}