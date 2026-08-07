package com.dinsaren.springbootjwtapi.controllers.rest;

import com.dinsaren.springbootjwtapi.models.User;
import com.dinsaren.springbootjwtapi.models.req.ChangePasswordReq;
import com.dinsaren.springbootjwtapi.payload.response.MessageRes;
import com.dinsaren.springbootjwtapi.repository.UserRepository;
import com.dinsaren.springbootjwtapi.services.AuthenticationUtilService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/app/user")
@Slf4j
@Tag(name = "User", description = "Current user profile and password management")
public class UserController {

    private final AuthenticationUtilService authenticationUtilService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserController(
            AuthenticationUtilService authenticationUtilService,
            PasswordEncoder passwordEncoder,
            UserRepository userRepository) {

        this.authenticationUtilService = authenticationUtilService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

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

            //------------------------------------------
            // Get Current Login User
            //------------------------------------------

            User user = authenticationUtilService.checkUser();

            if (user == null) {

                response.getUserNotFound();

                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(response);
            }

            //------------------------------------------
            // Check Old Password
            //------------------------------------------

            if (!passwordEncoder.matches(
                    request.getOldPassword(),
                    user.getPassword())) {

                response.setOldPasswordNotMatch();

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(response);
            }

            //------------------------------------------
            // Check Confirm Password
            //------------------------------------------

            if (!request.getNewPassword()
                    .equals(request.getConfirmPassword())) {

                response.setConfirmPasswordNotMatch();

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(response);
            }

            //------------------------------------------
            // Update Password
            //------------------------------------------

            user.setPassword(
                    passwordEncoder.encode(request.getNewPassword()));

            userRepository.save(user);

            //------------------------------------------
            // Success
            //------------------------------------------

            response.setMessageSuccess(user);

            return ResponseEntity.ok(response);

        } catch (Exception ex) {

            log.error("Change password error", ex);

            response.setMessage("Internal Server Error");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

}