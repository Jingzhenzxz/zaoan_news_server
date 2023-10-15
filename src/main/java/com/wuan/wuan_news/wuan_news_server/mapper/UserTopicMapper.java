package com.wuan.wuan_news.wuan_news_server.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/10/15/ 10:12
 * @description
 */
@Mapper
public interface UserTopicMapper {
    List<Long> getTopicIdsByUserId(Long userId);
    List<Long> getUserIdsByTopicId(Long topicId);
    int insertUserTopic(Long userId, Long topicId);
    int deleteUserTopic(Long userId, Long topicId);
}
