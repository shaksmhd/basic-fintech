package com.shaka.finte.controler;

import com.shaka.finte.DTO.CreateUserRequest;
import com.shaka.finte.DTO.CreateUserResponse;
import com.shaka.finte.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<CreateUserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        log.debug("Received request to create user: {}", request);
        try {
            CreateUserResponse response = userService.createUser(request);
            log.info("User created successfully: {}", response);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            CreateUserResponse errorResponse = new CreateUserResponse();
            errorResponse.setMessage("User creation failed: " + e.getMessage());
            log.error("Error creating user: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
