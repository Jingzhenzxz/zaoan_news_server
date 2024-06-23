package com.wuan.wuan_news.wuan_news_server.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 用户与媒体之间的关系
 * @author Jingzhen
 * @TableName user_media
 */
@TableName(value ="user_media")
@Data
public class UserMedia implements Serializable {
    /**
     * 用户编码
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 媒体编码
     */
    @TableField(value = "media_id")
    private Long mediaId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}