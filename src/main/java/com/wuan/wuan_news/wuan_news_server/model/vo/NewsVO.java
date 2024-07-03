package com.wuan.wuan_news.wuan_news_server.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.wuan.wuan_news.wuan_news_server.model.entity.News;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author Jingzhen
 */
@Data
public class NewsVO implements Serializable {
    /**
     * 新闻编码
     */
    private Long id;

    /**
     * 新闻标题
     */
    private String title;

    /**
     * 新闻简介
     */
    private String description;

    /**
     * 预览图
     */
    private String previewImage;

    /**
     * 发表日期
     */
    private LocalDateTime pubDate;

    /**
     * 新闻地址
     */
    private String link;

    /**
     * 作者
     */
    private String author;

    /**
     * 媒体名称
     */
    private String mediaName;

    /**
     * 媒体编码
     */
    private Long mediaId;

    private static final long serialVersionUID = 1L;
}
