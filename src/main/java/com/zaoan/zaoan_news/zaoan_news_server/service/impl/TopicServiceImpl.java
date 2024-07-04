package com.zaoan.zaoan_news.zaoan_news_server.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zaoan.zaoan_news.zaoan_news_server.common.ErrorCode;
import com.zaoan.zaoan_news.zaoan_news_server.constant.CommonConstant;
import com.zaoan.zaoan_news.zaoan_news_server.exception.BusinessException;
import com.zaoan.zaoan_news.zaoan_news_server.model.dto.topic.TopicQueryRequest;
import com.zaoan.zaoan_news.zaoan_news_server.mapper.TopicMapper;
import com.zaoan.zaoan_news.zaoan_news_server.model.entity.News;
import com.zaoan.zaoan_news.zaoan_news_server.model.entity.Topic;
import com.zaoan.zaoan_news.zaoan_news_server.model.entity.UserTopicFollowing;
import com.zaoan.zaoan_news.zaoan_news_server.model.vo.TopicVO;
import com.zaoan.zaoan_news.zaoan_news_server.service.NewsService;
import com.zaoan.zaoan_news.zaoan_news_server.service.TopicService;
import com.zaoan.zaoan_news.zaoan_news_server.service.UserTopicFollowingService;
import com.zaoan.zaoan_news.zaoan_news_server.util.SqlUtils;
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
    private final UserTopicFollowingService userTopicFollowingService;
    private final NewsService newsService;

    public TopicServiceImpl(UserTopicFollowingService userTopicFollowingService, NewsService newsService) {
        this.userTopicFollowingService = userTopicFollowingService;
        this.newsService = newsService;
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
            QueryWrapper<UserTopicFollowing> userTopicQueryWrapper = new QueryWrapper<>();
            userTopicQueryWrapper.eq("user_id", topicQueryRequest.getUserId());
             topicIds = userTopicFollowingService.list(userTopicQueryWrapper).stream().map(UserTopicFollowing::getTopicId).collect(Collectors.toList());
        }
        QueryWrapper<Topic> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.in(!topicIds.isEmpty(), "id", topicIds);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public TopicVO topicToTopicVO(Topic topic) {
        if (topic == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        TopicVO topicVO = new TopicVO();
        topicVO.setId(topic.getId());
        topicVO.setTopicName(topic.getName());

        List<News> listOfNews = new ArrayList<>();
        listOfNews.add(newsService.getById(topic.getLatestNews1Id()));
        listOfNews.add(newsService.getById(topic.getLatestNews2Id()));
        listOfNews.add(newsService.getById(topic.getLatestNews3Id()));
        topicVO.setListOfNews(listOfNews);

        topicVO.setNewContentTodayCount(topicVO.getNewContentTodayCount());
        return topicVO;
    }
}
