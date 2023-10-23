package com.wuan.wuan_news.wuan_news_server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/07/30/ 16:38
 * @description
 */
@Data
@AllArgsConstructor
public class LoginResponse {
    private String username;
    private String email;
    private String token;
    private String errorMessage;
}
