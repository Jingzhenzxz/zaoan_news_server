package com.wuan.wuan_news.wuan_news_server.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 * @author Jingzhen
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    @TableField(value = "user_name")
    private String userName;

    /**
     * 
     */
    @TableField(value = "email")
    private String email;

    /**
     * 
     */
    @TableField(value = "password")
    private String password;

    /**
     * 用户角色：admin/user/ban
     */
    @TableField(value = "user_role")
    private String userRole;

    /**
     * 用户头像
     */
    @TableField(value = "user_avatar")
    private String userAvatar;

    /**
     * 用户简介
     */
    @TableField(value = "user_profile")
    private String userProfile;

    /**
     *
     */
    @TableField(value = "created_at")
    private LocalDateTime createdAt;

    /**
     *
     */
    @TableField(value = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * 是否删除
     */
    @TableField(value = "is_delete")
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}