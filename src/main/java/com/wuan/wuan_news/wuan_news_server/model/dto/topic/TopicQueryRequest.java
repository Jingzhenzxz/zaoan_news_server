package com.wuan.wuan_news.wuan_news_server.model.dto.topic;

import com.wuan.wuan_news.wuan_news_server.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/09/02/ 10:04
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TopicQueryRequest extends PageRequest implements Serializable {
    /**
     * 话题编码
     */
    private Long id;

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
