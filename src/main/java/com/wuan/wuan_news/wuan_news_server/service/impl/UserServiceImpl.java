package com.wuan.wuan_news.wuan_news_server.service.impl;

import com.wuan.wuan_news.wuan_news_server.dto.UserDTO;
import com.wuan.wuan_news.wuan_news_server.exception.UserException;
import com.wuan.wuan_news.wuan_news_server.mapper.UserMapper;
import com.wuan.wuan_news.wuan_news_server.model.User;
import com.wuan.wuan_news.wuan_news_server.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/07/30/ 16:40
 * @description
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserDTO createNewUser(UserDTO newUser) {
        int result = userMapper.insert(newUser);
        if (result == 0) {
            throw new UserException("创建新用户失败");
        } else {
            return newUser;
        }
    }

    @Override
    public UserDTO findByEmail(String email) {
        User user = userMapper.getUserByEmail(email);
        if (user == null) {
            throw new UserException("没有找到该 Email 对应的用户");
        }
        return convertUserModelToUserDTO(user);
    }

    private UserDTO convertUserModelToUserDTO(User user) {
        // Convert the User object to UserDto object
        if (user == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());

        return userDTO;
    }

    private User convertUserDTOToUserModel(UserDTO userDTO) {
        // Convert the UserDto object to User object
        if (userDTO == null) {
            return null;
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());

        return user;
    }
}
