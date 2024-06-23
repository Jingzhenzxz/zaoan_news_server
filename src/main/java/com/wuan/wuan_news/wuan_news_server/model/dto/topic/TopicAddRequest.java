package com.wuan.wuan_news.wuan_news_server.model.dto.topic;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/09/02/ 10:04
 * @description
 */
@Data
public class TopicAddRequest implements Serializable {
    /**
     * 话题名称
     */
    private String name;

    /**
     * 所属用户的编码
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}