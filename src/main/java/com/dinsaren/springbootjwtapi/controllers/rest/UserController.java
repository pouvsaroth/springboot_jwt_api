package com.dinsaren.springbootjwtapi.controllers.rest;

import com.dinsaren.springbootjwtapi.models.User;
import com.dinsaren.springbootjwtapi.models.req.ChangePasswordReq;
import com.dinsaren.springbootjwtapi.models.res.UploadFileRes;
import com.dinsaren.springbootjwtapi.models.school.Student;
import com.dinsaren.springbootjwtapi.models.school.Teacher;
import com.dinsaren.springbootjwtapi.payload.response.MessageRes;
import com.dinsaren.springbootjwtapi.repository.StudentRepository;
import com.dinsaren.springbootjwtapi.repository.TeacherRepository;
import com.dinsaren.springbootjwtapi.repository.UserRepository;
import com.dinsaren.springbootjwtapi.services.AuthenticationUtilService;
import com.dinsaren.springbootjwtapi.services.UploadFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/app/user")
@Slf4j
@Tag(name = "User", description = "Current user profile and password management")
public class UserController {

    private final AuthenticationUtilService authenticationUtilService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UploadFileService uploadFileService;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    public UserController(
            AuthenticationUtilService authenticationUtilService,
            PasswordEncoder passwordEncoder,
            UserRepository userRepository,
            UploadFileService uploadFileService,
            TeacherRepository teacherRepository,
            StudentRepository studentRepository) {

        this.authenticationUtilService = authenticationUtilService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.uploadFileService = uploadFileService;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
    }

    // =========================================================
    // CHANGE PASSWORD
    // =========================================================

    @Operation(
            summary = "Change Password",
            description = "Change password for currently logged in user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/change-password")
    public ResponseEntity<MessageRes> changePassword(
            @RequestBody ChangePasswordReq request) {

        MessageRes response = new MessageRes();

        try {

            User user = authenticationUtilService.checkUser();

            if (user == null) {

                response.getUserNotFound();

                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(response);
            }

            if (!passwordEncoder.matches(
                    request.getOldPassword(),
                    user.getPassword())) {

                response.setOldPasswordNotMatch();

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(response);
            }

            if (!request.getNewPassword()
                    .equals(request.getConfirmPassword())) {

                response.setConfirmPasswordNotMatch();

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(response);
            }

            user.setPassword(
                    passwordEncoder.encode(request.getNewPassword()));

            userRepository.save(user);

            response.setMessageSuccess(user);

            return ResponseEntity.ok(response);

        } catch (Exception ex) {

            log.error("Change password error", ex);

            response.setMessage("Internal Server Error");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    // =========================================================
    // UPLOAD PROFILE PHOTO
    // =========================================================

    @Operation(
            summary = "Upload Profile Photo",
            description = "Upload teacher/student profile image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Upload success"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(
            value = "/upload-photo",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadPhoto(
            @RequestParam("file") MultipartFile file) {

        try {

            User user = authenticationUtilService.checkUser();

            if (user == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("User not found");
            }

            UploadFileRes uploadResult =
                    uploadFileService.uploadFile(file);

            // Update Teacher
            Optional<Teacher> teacher =
                    teacherRepository.findByUser(user);

            if (teacher.isPresent()) {

                teacher.get().setPhoto(uploadResult.getFileName());

                teacherRepository.save(teacher.get());

                return ResponseEntity.ok(uploadResult);
            }

            // Update Student
            Optional<Student> student =
                    studentRepository.findByUser(user);

            if (student.isPresent()) {

                student.get().setPhoto(uploadResult.getFileName());

                studentRepository.save(student.get());

                return ResponseEntity.ok(uploadResult);
            }

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Teacher or Student profile not found.");

        } catch (Exception ex) {

            log.error("Upload profile photo error", ex);

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ex.getMessage());
        }
    }

}