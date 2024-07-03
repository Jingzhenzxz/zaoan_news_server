package com.wuan.wuan_news.wuan_news_server.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @program: wuan_news_server
 * @description:
 * @author: Jingzhen
 * @create: 2024-07-02 17:20
 **/
@Data
public class MediaVO implements Serializable {

    /**
     * 媒体编码
     */
    private Long id;

    /**
     * 媒体名称
     */
    private String name;

    /**
     * rss地址
     */
    private String rssLink;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    private static final long serialVersionUID = 1L;
}
