package com.wuan.wuan_news.wuan_news_server.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户创建请求
 * @author Jingzhen
 */
@Data
public class UserAddRequest implements Serializable {

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 账号
     */
    private String email;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户角色: user, admin
     */
    private String userRole;

    private static final long serialVersionUID = 1L;
}