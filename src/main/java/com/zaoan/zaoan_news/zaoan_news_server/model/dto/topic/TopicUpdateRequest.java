package com.zaoan.zaoan_news.zaoan_news_server.model.dto.topic;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @description
 */
@Data
public class TopicUpdateRequest implements Serializable {
    /**
     * 话题编码
     * 这个必须有，因为id是不变的，name是可变的。必须有一个不变属性来确定要更新的topic。
     */
    @NotEmpty(message = "话题编码不能为空")
    private Long id;

    /**
     * 话题名称
     */
    private String name;

    /**
     * 所属用户的编码
     */
    @NotEmpty(message = "用户编码不能为空")
    private Long userId;

    private static final long serialVersionUID = 1L;
}
