package com.wuan.wuan_news.wuan_news_server.controller;

import com.wuan.wuan_news.wuan_news_server.dto.*;
import com.wuan.wuan_news.wuan_news_server.service.AuthenticationService;
import com.wuan.wuan_news.wuan_news_server.util.JwtUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/07/30/ 12:58
 * @description
 */
@Api(tags = "Authentication Endpoints", value = "Endpoints for user registration and login")
@RestController
@RequestMapping("/api/authentication")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, JwtUtil jwtUtil) {
        this.authenticationService = authenticationService;
        this.jwtUtil = jwtUtil;
    }

    @ApiOperation(value = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User registered successfully"),
            @ApiResponse(code = 400, message = "Bad Request, invalid user input")
    })
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @ApiParam(value = "Registration request object", required = true)
            @Valid @RequestBody RegisterRequest registerRequest) {
        UserDTO userDTO = authenticationService.register(registerRequest);
        RegisterResponse registerResponse = new RegisterResponse(userDTO.getUsername(), userDTO.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(registerResponse);
    }

    @ApiOperation(value = "Login with email and password")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Login successful"),
            @ApiResponse(code = 400, message = "Bad Request, invalid login details"),
            @ApiResponse(code = 401, message = "Unauthorized, invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @ApiParam(value = "Login request object", required = true)
            @Valid @RequestBody LoginRequest loginRequest) {
        UserDTO userDTO = authenticationService.login(loginRequest.getEmail(), loginRequest.getPassword());
        String token = jwtUtil.generateToken(userDTO);
        LoginResponse loginResponse = new LoginResponse(userDTO.getUsername(), userDTO.getEmail(), token);
        return ResponseEntity.ok(loginResponse);
    }
}
