package com.wuan.wuan_news.wuan_news_server.service;

import com.wuan.wuan_news.wuan_news_server.dto.UserDTO;
import com.wuan.wuan_news.wuan_news_server.model.User;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/07/30/ 16:37
 * @description
 */
public interface UserService {
    UserDTO createNewUser(User newUser);
    UserDTO findByEmail(String email);
    Long getUserIdByEmail(String email);
    String getPasswordByEmail(String email);
    String getPasswordByUserId(Long userId);
}
