package com.wuan.wuan_news.wuan_news_server.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/07/30/ 13:29
 * @description
 */
@Data
public class UserRegisterRequest implements Serializable {
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
    private static final long serialVersionUID = 3191241716373120793L;
}
