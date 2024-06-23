package com.wuan.wuan_news.wuan_news_server.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 删除请求
 * @author Jingzhen
 */
@Data
public class DeleteRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}