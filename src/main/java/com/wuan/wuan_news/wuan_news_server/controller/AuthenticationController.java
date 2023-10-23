package com.wuan.wuan_news.wuan_news_server.controller;

import com.wuan.wuan_news.wuan_news_server.dto.*;
import com.wuan.wuan_news.wuan_news_server.exception.InvalidPasswordException;
import com.wuan.wuan_news.wuan_news_server.exception.UserNotFoundException;
import com.wuan.wuan_news.wuan_news_server.service.AuthenticationService;
import com.wuan.wuan_news.wuan_news_server.service.UserService;
import com.wuan.wuan_news.wuan_news_server.util.JwtUtil;
import com.wuan.wuan_news.wuan_news_server.util.PasswordUtil;
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
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, UserService userService, JwtUtil jwtUtil) {
        this.authenticationService = authenticationService;
        this.userService = userService;
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

        // 检查邮箱格式
        String email = registerRequest.getEmail();
        if (email == null || !email.contains("@") || !email.contains(".")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RegisterResponse("", "", "Invalid email format."));
        }

        // 检查邮箱是否已经存在
        if (userService.findByEmail(registerRequest.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RegisterResponse("", "", "Email already exists."));
        }

        // 检查密码长度
        String password = registerRequest.getPassword();
        if (password == null || password.length() < 8) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RegisterResponse("", "", "Password must be at least 8 characters long."));
        }

        // 检查密码和确认密码是否相同
        if (!password.equals(registerRequest.getConfirmPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RegisterResponse("", "", "Password and confirmation password do not match."));
        }

        UserDTO userDTO = authenticationService.register(registerRequest);
        RegisterResponse registerResponse = new RegisterResponse(userDTO.getUsername(), userDTO.getEmail(), "");
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
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        // 先看看该用户是否存在
        UserDTO userDTO = userService.findByEmail(email);
        if (userDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse("", "","", "没有找到 " + email + " 对应的用户"));
        }

        // 如果用户存在，就检查密码是否正确
        String hashedPassword = userService.getPasswordByEmail(email);
        boolean passwordMatch = PasswordUtil.verifyPassword(password, hashedPassword);
        if (!passwordMatch) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponse("", "", "", "密码不正确"));
        }

        // 没用到 authenticationService.login 方法
        // UserDTO userDTO = authenticationService.login(loginRequest.getEmail(), loginRequest.getPassword());
        String token = jwtUtil.generateToken(userDTO);
        LoginResponse loginResponse = new LoginResponse(userDTO.getUsername(), userDTO.getEmail(), token, "");
        return ResponseEntity.ok(loginResponse);
    }
}
