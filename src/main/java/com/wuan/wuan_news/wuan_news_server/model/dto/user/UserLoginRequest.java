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
public class UserLoginRequest implements Serializable {
    private String email;
    private String password;
    private static final long serialVersionUID = 3191241716373120793L;
}
