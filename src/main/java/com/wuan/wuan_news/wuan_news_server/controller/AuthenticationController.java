package com.wuan.wuan_news.wuan_news_server.controller;

import com.wuan.wuan_news.wuan_news_server.dto.LoginRequest;
import com.wuan.wuan_news.wuan_news_server.dto.LoginResponse;
import com.wuan.wuan_news.wuan_news_server.dto.RegisterRequest;
import com.wuan.wuan_news.wuan_news_server.dto.UserDTO;
import com.wuan.wuan_news.wuan_news_server.exception.PasswordException;
import com.wuan.wuan_news.wuan_news_server.exception.UserException;
import com.wuan.wuan_news.wuan_news_server.service.AuthenticationService;
import com.wuan.wuan_news.wuan_news_server.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/07/30/ 12:58
 * @description
 */

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

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        try {
            UserDTO userDTO = authenticationService.register(registerRequest);
            return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
        } catch (UserException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            UserDTO userDTO = authenticationService.login(loginRequest.getEmail(), loginRequest.getPassword());
            String token = jwtUtil.generateToken(userDTO);
            return ResponseEntity.ok(new LoginResponse(userDTO, token));
        } catch (UserException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        } catch (PasswordException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
        }
    }

}
