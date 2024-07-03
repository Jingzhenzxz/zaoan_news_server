package com.wuan.wuan_news.wuan_news_server.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 * @author Jingzhen
 * @TableName news_topic
 */
@TableName(value ="news_topic")
@Data
public class NewsTopic implements Serializable {
    /**
     * news_id and topic_id together act as a composite key.
     * Here they are not annotated with @TableId.
     */

    /**
     * 新闻编码
     */
    private Long newsId;

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
     * 更新时间
     */
    @TableField(value = "updated_at")
    private LocalDateTime updatedAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}