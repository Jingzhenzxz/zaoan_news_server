package com.wuan.wuan_news.wuan_news_server.dto;

import lombok.Data;

import javax.validation.Valid;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/07/30/ 13:29
 * @description
 */
@Data
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
}
