package com.wuan.wuan_news.wuan_news_server.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuan.wuan_news.wuan_news_server.common.ErrorCode;
import com.wuan.wuan_news.wuan_news_server.constant.CommonConstant;
import com.wuan.wuan_news.wuan_news_server.exception.BusinessException;
import com.wuan.wuan_news.wuan_news_server.model.dto.topic.TopicQueryRequest;
import com.wuan.wuan_news.wuan_news_server.mapper.TopicMapper;
import com.wuan.wuan_news.wuan_news_server.model.entity.Topic;
import com.wuan.wuan_news.wuan_news_server.model.entity.UserTopic;
import com.wuan.wuan_news.wuan_news_server.service.TopicService;
import com.wuan.wuan_news.wuan_news_server.service.UserTopicService;
import com.wuan.wuan_news.wuan_news_server.util.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/09/02/ 15:41
 * @description
 */
@Service
public class TopicServiceImpl extends ServiceImpl<TopicMapper, Topic> implements TopicService {
    private final UserTopicService userTopicService;

    public TopicServiceImpl(UserTopicService userTopicService) {
        this.userTopicService = userTopicService;
    }

    @Override
    public Wrapper<Topic> getQueryWrapper(TopicQueryRequest topicQueryRequest) {
        if (topicQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = topicQueryRequest.getId();
        String name = topicQueryRequest.getName();
        String sortField = topicQueryRequest.getSortField();
        String sortOrder = topicQueryRequest.getSortOrder();
        List<Long> topicIds = new ArrayList<>();
        if (topicQueryRequest.getUserId() != null) {
            QueryWrapper<UserTopic> userTopicQueryWrapper = new QueryWrapper<>();
            userTopicQueryWrapper.eq("user_id", topicQueryRequest.getUserId());
             topicIds = userTopicService.list(userTopicQueryWrapper).stream().map(UserTopic::getTopicId).collect(Collectors.toList());
        }
        QueryWrapper<Topic> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.in(!topicIds.isEmpty(), "id", topicIds);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }
}
