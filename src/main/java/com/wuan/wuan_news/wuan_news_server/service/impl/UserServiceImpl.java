package com.wuan.wuan_news.wuan_news_server.service.impl;

import com.wuan.wuan_news.wuan_news_server.dto.UserDTO;
import com.wuan.wuan_news.wuan_news_server.exception.UserAttributeException;
import com.wuan.wuan_news.wuan_news_server.exception.UserCreationFailedException;
import com.wuan.wuan_news.wuan_news_server.exception.UserNotFoundException;
import com.wuan.wuan_news.wuan_news_server.mapper.UserMapper;
import com.wuan.wuan_news.wuan_news_server.model.User;
import com.wuan.wuan_news.wuan_news_server.service.UserService;
import com.wuan.wuan_news.wuan_news_server.util.UserUtil;
import org.springframework.stereotype.Service;

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
    public UserDTO createNewUser(User newUser) {
        int result = userMapper.insert(newUser);
        if (result == 0) {
            throw new UserCreationFailedException("创建新用户失败");
        } else {
            return userUtil.convertUserModelToUserDTO(newUser);
        }
    }

    @Override
    public UserDTO findByEmail(String email) {
        User user = userMapper.getUserByEmail(email);
        if (user == null) {
            /* 注意，这里不能抛出错误，因为在第一个用户注册的时候，我们会调用 findByEmail 方法检查该用户是否存在，此时肯定
            是不不存在的，所以应该返回 null 而不是抛出异常。 */
            return null;
        }
        return userUtil.convertUserModelToUserDTO(user);
    }

    @Override
    public Long getUserIdByEmail(String email) {
        Long userId = userMapper.getUserIdByEmail(email);
        if (userId == null) {
            throw  new UserNotFoundException("找不到该 Email 对应的用户");
        }
        return userId;
    }

    @Override
    public String getPasswordByEmail(String email) {
        String password = userMapper.getPasswordByEmail(email);
        if (password == null) {
            throw new UserAttributeException("用户的密码不存在");
        }
        return password;
    }

    @Override
    public String getPasswordByUserId(Long userId) {
        String password = userMapper.getPasswordByUserId(userId);
        if (password == null) {
            throw new UserAttributeException("用户的密码不存在");
        }
        return password;
    }
}
