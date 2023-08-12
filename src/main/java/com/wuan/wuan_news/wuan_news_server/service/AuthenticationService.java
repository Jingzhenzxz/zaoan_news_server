package com.wuan.wuan_news.wuan_news_server.service;

import com.wuan.wuan_news.wuan_news_server.dto.RegisterRequest;
import com.wuan.wuan_news.wuan_news_server.dto.UserDTO;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/07/30/ 13:27
 * @description
 */
public interface AuthenticationService {
    UserDTO register(RegisterRequest registerRequest);

    UserDTO login(String email, String password);
}
