package com.wuan.wuan_news.wuan_news_server.service;

import com.wuan.wuan_news.wuan_news_server.dto.UserDTO;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/07/30/ 16:37
 * @description
 */
public interface UserService {
    UserDTO createNewUser(UserDTO newUser);
    UserDTO findByEmail(String email);
}
