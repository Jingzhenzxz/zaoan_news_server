package com.wuan.wuan_news.wuan_news_server.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wuan.wuan_news.wuan_news_server.annotation.AuthCheck;
import com.wuan.wuan_news.wuan_news_server.common.BaseResponse;
import com.wuan.wuan_news.wuan_news_server.common.ErrorCode;
import com.wuan.wuan_news.wuan_news_server.common.ResultUtils;
import com.wuan.wuan_news.wuan_news_server.constant.UserConstant;
import com.wuan.wuan_news.wuan_news_server.model.dto.topic.TopicQueryRequest;
import com.wuan.wuan_news.wuan_news_server.model.dto.topicCard.TopicCardQueryRequest;
import com.wuan.wuan_news.wuan_news_server.model.entity.News;
import com.wuan.wuan_news.wuan_news_server.model.entity.NewsTopic;
import com.wuan.wuan_news.wuan_news_server.model.entity.Topic;
import com.wuan.wuan_news.wuan_news_server.model.vo.TopicCardVO;
import com.wuan.wuan_news.wuan_news_server.service.NewsService;
import com.wuan.wuan_news.wuan_news_server.service.NewsTopicService;
import com.wuan.wuan_news.wuan_news_server.service.TopicService;
import com.wuan.wuan_news.wuan_news_server.service.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/09/02/ 9:54
 * @description
 */
@RestController
@RequestMapping("/topicCard")
public class TopicCardController {
    private final TopicService topicService;
    private final NewsService newsService;
    private final NewsTopicService newsTopicService;

    public TopicCardController(TopicService topicService, NewsService newsService, NewsTopicService newsTopicService) {
        this.topicService = topicService;
        this.newsService = newsService;
        this.newsTopicService = newsTopicService;
    }

    /**
     * 获取所有话题卡片
     */
    @GetMapping("/list")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse listTopicCards(@RequestBody TopicCardQueryRequest topicCardQueryRequest, HttpServletRequest request) {
        // 查询所有指定的topic
        TopicQueryRequest topicQueryRequest = new TopicQueryRequest();
        BeanUtils.copyProperties(topicCardQueryRequest, topicQueryRequest);
        Wrapper<Topic> topicQueryWrapper = topicService.getQueryWrapper(topicQueryRequest);
        List<Topic> topicList = topicService.list(topicQueryWrapper);

        List<TopicCardVO> topicCardVOList = topicList.stream().map(topic -> {
            QueryWrapper<NewsTopic> newsTopicQueryWrapper = new QueryWrapper<>();
            newsTopicQueryWrapper.eq("topic_id", topic.getId());
            List<Long> newsId = newsTopicService.list(newsTopicQueryWrapper).stream().map(NewsTopic::getNewsId).collect(Collectors.toList());
            List<News> newsOfTopic = newsId.stream().map(newsService::getById).collect(Collectors.toList());

            List<News> threeNewsOfTopic = newsOfTopic.stream()
                    .sorted((n1, n2) -> n2.getPubDate().compareTo(n1.getPubDate()))
                    .limit(3)
                    .collect(Collectors.toList());

            long newContentTodayCount = newsOfTopic.stream()
                    .filter(singleNews -> singleNews.getCreatedAt().toLocalDate().equals(LocalDate.now()))
                    .count();

            return new TopicCardVO(topic.getName(), threeNewsOfTopic, newContentTodayCount);
        }).collect(Collectors.toList());

        return ResultUtils.success(topicCardVOList);
    }

    /**
     * 根据话题名称获取单个话题卡片
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse getTopicCardByTopicName(@RequestBody String topicName, HttpServletRequest request) {
        QueryWrapper<Topic> topicQueryWrapper = new QueryWrapper<>();
        topicQueryWrapper.eq("topic_name", topicName);
        Topic topic = topicService.getOne(topicQueryWrapper);
        if (topic == null) {
            return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR, "请求的话题不存在");
        }

        // 查询topicId对应的所有newsId
        QueryWrapper<NewsTopic> newsTopicQueryWrapper = new QueryWrapper<>();
        newsTopicQueryWrapper.eq("topic_id", topic.getId());
        List<Long> newsId = newsTopicService.list(newsTopicQueryWrapper).stream().map(NewsTopic::getNewsId).collect(Collectors.toList());
        // 根据newsId查询news
        List<News> newsOfTopic = newsId.stream().map(newsService::getById).collect(Collectors.toList());

        // 获取最新的三条news
        List<News> threeNewsOfTopic = newsOfTopic.stream()
                .sorted((n1, n2) -> n2.getPubDate().compareTo(n1.getPubDate()))
                .limit(3)
                .collect(Collectors.toList());

        // 计算今天更新了几条
        long newContentTodayCount = newsOfTopic.stream()
                .filter(singleNews -> singleNews.getCreatedAt().toLocalDate().equals(LocalDate.now()))
                .count();

        return ResultUtils.success(new TopicCardVO(topic.getName(), threeNewsOfTopic, newContentTodayCount));
    }
}
