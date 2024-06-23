package com.wuan.wuan_news.wuan_news_server.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Jingzhen
 * @TableName user_topic
 */
@TableName(value = "user_topic")
@Data
public class UserTopic implements Serializable {
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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}