package com.zaoan.zaoan_news.zaoan_news_server.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 * @TableName media
 */
@TableName(value ="media")
@Data
public class Media implements Serializable {
    /**
     * 媒体编码
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 媒体名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * rss地址
     */
    @TableField(value = "rss_link")
    private String rssLink;

    /**
     * 创建时间
     */
    @TableField(value = "created_at")
    private LocalDateTime createdAt;

    /**
     * 修改时间
     */
    @TableField(value = "updated_at")
    private LocalDateTime updatedAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}