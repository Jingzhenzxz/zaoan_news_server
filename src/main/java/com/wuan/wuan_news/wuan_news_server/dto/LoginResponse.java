package com.wuan.wuan_news.wuan_news_server.dto;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/07/30/ 16:38
 * @description
 */
public class LoginResponse {
    private String token;
    private UserDTO userDTO;

    public LoginResponse(UserDTO userDTO, String token) {
        this.token = token;
        this.userDTO = userDTO;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserDTO getUser() {
        return userDTO;
    }

    public void setUser(UserDTO userDTO) {
        this.userDTO = userDTO;
    }
}
