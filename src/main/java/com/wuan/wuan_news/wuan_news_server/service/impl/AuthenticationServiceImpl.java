package com.wuan.wuan_news.wuan_news_server.service.impl;

import com.wuan.wuan_news.wuan_news_server.dto.RegisterRequest;
import com.wuan.wuan_news.wuan_news_server.dto.UserDTO;
import com.wuan.wuan_news.wuan_news_server.exception.*;
import com.wuan.wuan_news.wuan_news_server.service.AuthenticationService;
import com.wuan.wuan_news.wuan_news_server.service.UserService;
import com.wuan.wuan_news.wuan_news_server.util.JwtUtil;
import com.wuan.wuan_news.wuan_news_server.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/07/30/ 13:36
 * @description
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthenticationServiceImpl(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public UserDTO login(String email, String password) {
        UserDTO userDTO = userService.findByEmail(email);

        if (userDTO == null) {
            throw new UserNotFoundException("没有找到 " + email + " 对应的用户");
        }

        boolean passwordMatch = PasswordUtil.verifyPassword(password, userDTO.getPassword());
        if (!passwordMatch) {
            throw new InvalidPasswordException("输入的密码有误");
        }

        return userDTO;
    }

    @Override
    public UserDTO register(RegisterRequest registerRequest) {
        // 检查密码和确认密码是否相同
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            throw new PasswordMismatchException("Password and confirm password do not match");
        }

        // 检查邮箱是否已经存在
        if (userService.findByEmail(registerRequest.getEmail()) != null) {
            throw new UserEmailAlreadyExistsException("UserEmail already exists!");
        }

        // 创建新用户
        UserDTO newUser = new UserDTO();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setEmail(registerRequest.getEmail());
        newUser.setPassword(PasswordUtil.encode(registerRequest.getPassword()));

        // 将新用户插入数据库
        UserDTO userDTO = userService.createNewUser(newUser);
        if (userDTO == null) {
            throw new UserCreationFailedException("创建新用户失败");
        }

        // 返回新创建的用户
        return newUser;
    }
}