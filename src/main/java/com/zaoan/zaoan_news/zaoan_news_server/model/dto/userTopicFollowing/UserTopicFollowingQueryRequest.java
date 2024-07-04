package com.zaoan.zaoan_news.zaoan_news_server.model.dto.userTopicFollowing;

import com.zaoan.zaoan_news.zaoan_news_server.common.PageRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * @author Jingzhen
 * @date 2024/6/30 17:54
 * @description 
 */
@Data
public class UserTopicFollowingQueryRequest extends PageRequest implements Serializable {
    /**
     * 用户编码
     */
    private Long userId;

    /**
     * 话题编码
     */
    private Long topicId;

    private static final long serialVersionUID = 1L;
}
