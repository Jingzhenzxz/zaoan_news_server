package com.wuan.wuan_news.wuan_news_server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/08/06/ 11:34
 * @description
 */
@Data
@AllArgsConstructor
public class RegisterResponse {
    private String username;
    private String email;
}
