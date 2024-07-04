package com.zaoan.zaoan_news.zaoan_news_server.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Jingzhen
 * @TableName user_topic_following
 */
@TableName(value = "user_topic_following")
@Data
public class UserTopicFollowing implements Serializable {
    /**
     * news_id and topic_id together act as a composite key.
     * Here they are not annotated with @TableId.
     */

    /**
     * 用户编码
     */
    private Long userId;

    /**
     * 话题编码
     */
    private Long topicId;

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