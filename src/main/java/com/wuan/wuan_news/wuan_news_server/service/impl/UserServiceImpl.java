package com.wuan.wuan_news.wuan_news_server.service.impl;

import com.wuan.wuan_news.wuan_news_server.dto.UserDTO;
import com.wuan.wuan_news.wuan_news_server.exception.UserException;
import com.wuan.wuan_news.wuan_news_server.mapper.UserMapper;
import com.wuan.wuan_news.wuan_news_server.model.User;
import com.wuan.wuan_news.wuan_news_server.service.UserService;
import com.wuan.wuan_news.wuan_news_server.util.UserUtil;
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
    private final UserUtil userUtil;

    public UserServiceImpl(UserMapper userMapper, UserUtil userUtil) {
        this.userMapper = userMapper;
        this.userUtil = userUtil;
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
        return userUtil.convertUserModelToUserDTO(user);
    }
}