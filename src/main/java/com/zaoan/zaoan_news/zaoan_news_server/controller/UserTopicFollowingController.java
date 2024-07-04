package com.zaoan.zaoan_news.zaoan_news_server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zaoan.zaoan_news.zaoan_news_server.annotation.AuthCheck;
import com.zaoan.zaoan_news.zaoan_news_server.common.BaseResponse;
import com.zaoan.zaoan_news.zaoan_news_server.common.ErrorCode;
import com.zaoan.zaoan_news.zaoan_news_server.common.ResultUtils;
import com.zaoan.zaoan_news.zaoan_news_server.constant.UserConstant;
import com.zaoan.zaoan_news.zaoan_news_server.exception.BusinessException;
import com.zaoan.zaoan_news.zaoan_news_server.exception.ThrowUtils;
import com.zaoan.zaoan_news.zaoan_news_server.model.dto.userTopicFollowing.UserTopicFollowingAddRequest;
import com.zaoan.zaoan_news.zaoan_news_server.model.dto.userTopicFollowing.UserTopicFollowingDeleteRequest;
import com.zaoan.zaoan_news.zaoan_news_server.model.dto.userTopicFollowing.UserTopicFollowingQueryRequest;
import com.zaoan.zaoan_news.zaoan_news_server.model.entity.News;
import com.zaoan.zaoan_news.zaoan_news_server.model.entity.Topic;
import com.zaoan.zaoan_news.zaoan_news_server.model.entity.User;
import com.zaoan.zaoan_news.zaoan_news_server.model.entity.UserTopicFollowing;
import com.zaoan.zaoan_news.zaoan_news_server.model.vo.TopicVO;
import com.zaoan.zaoan_news.zaoan_news_server.service.NewsService;
import com.zaoan.zaoan_news.zaoan_news_server.service.TopicService;
import com.zaoan.zaoan_news.zaoan_news_server.service.UserService;
import com.zaoan.zaoan_news.zaoan_news_server.service.UserTopicFollowingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: zaoan_news_server
 * @description:
 * @author: Jingzhen
 * @create: 2024-06-30 17:50
 **/
@RestController
@RequestMapping("/userTopicFollowing")
public class UserTopicFollowingController {
    private final UserTopicFollowingService userTopicFollowingService;
    private final UserService userService;
    private final TopicService topicService;
    private final NewsService newsService;

    public UserTopicFollowingController(UserTopicFollowingService userTopicFollowingService, UserService userService, TopicService topicService, NewsService newsService) {
        this.userTopicFollowingService = userTopicFollowingService;
        this.userService = userService;
        this.topicService = topicService;
        this.newsService = newsService;
    }

    @PostMapping("/followTopic")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse<Boolean> followTopic(@RequestBody UserTopicFollowingAddRequest userTopicFollowingAddRequest, HttpServletRequest request) {
        if (userTopicFollowingAddRequest == null || userTopicFollowingAddRequest.getUserId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        User user = userService.getLoginUser(request);
        Long operatorId = user.getId();
        Long userId = userTopicFollowingAddRequest.getUserId();;

        if (!userId.equals(operatorId) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        Long topicId = userTopicFollowingAddRequest.getTopicId();

        UserTopicFollowing userTopicFollowing = new UserTopicFollowing();
        userTopicFollowing.setTopicId(topicId);
        userTopicFollowing.setUserId(userId);
        boolean followed = userTopicFollowingService.save(userTopicFollowing);
        if (followed) {
            return ResultUtils.success(true);
        }
        throw new BusinessException(ErrorCode.OPERATION_ERROR);
    }

    @PostMapping("/unfollowTopic")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse<Boolean> unfollowTopic(@RequestBody UserTopicFollowingDeleteRequest userTopicFollowingDeleteRequest, HttpServletRequest request) {
        if (userTopicFollowingDeleteRequest == null || userTopicFollowingDeleteRequest.getUserId() == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }

        User user = userService.getLoginUser(request);
        Long operatorId = user.getId();
        Long userId = userTopicFollowingDeleteRequest.getUserId();;

        if (!userId.equals(operatorId) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        Long topicId = userTopicFollowingDeleteRequest.getTopicId();

        QueryWrapper<UserTopicFollowing> userTopicFollowingQueryWrapper = new QueryWrapper<>();
        userTopicFollowingQueryWrapper.eq("topic_id", topicId);
        userTopicFollowingQueryWrapper.eq("user_id", userId);
        boolean deleted = userTopicFollowingService.remove(userTopicFollowingQueryWrapper);
        if (deleted) {
            return ResultUtils.success(true);
        }
        throw new BusinessException(ErrorCode.OPERATION_ERROR);
    }

    @PostMapping("/get")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse<Topic> getFollowingTopic(@RequestBody UserTopicFollowingQueryRequest userTopicFollowingQueryRequest, HttpServletRequest request) {
        if (userTopicFollowingQueryRequest == null || userTopicFollowingQueryRequest.getUserId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        User user = userService.getLoginUser(request);
        Long operatorId = user.getId();
        Long userId = userTopicFollowingQueryRequest.getUserId();;

        if (!userId.equals(operatorId) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        Long topicId = userTopicFollowingQueryRequest.getTopicId();

        QueryWrapper<UserTopicFollowing> userTopicFollowingQueryWrapper = new QueryWrapper<>();
        userTopicFollowingQueryWrapper.eq("topic_id", topicId);
        userTopicFollowingQueryWrapper.eq("user_id", userId);
        UserTopicFollowing userTopicFollowing = userTopicFollowingService.getOne(userTopicFollowingQueryWrapper);
        if (userTopicFollowing == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        Topic topic = topicService.getById(userTopicFollowing.getTopicId());
        ThrowUtils.throwIf(topic == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(topic);
    }

    @PostMapping("/get/vo")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse<TopicVO> getFollowingTopicVO(@RequestBody UserTopicFollowingQueryRequest userTopicFollowingQueryRequest, HttpServletRequest request) {
        if (userTopicFollowingQueryRequest == null || userTopicFollowingQueryRequest.getUserId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        User user = userService.getLoginUser(request);
        Long operatorId = user.getId();
        Long userId = userTopicFollowingQueryRequest.getUserId();;

        if (!userId.equals(operatorId) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        Long topicId = userTopicFollowingQueryRequest.getTopicId();

        QueryWrapper<UserTopicFollowing> userTopicFollowingQueryWrapper = new QueryWrapper<>();
        userTopicFollowingQueryWrapper.eq("topic_id", topicId);
        userTopicFollowingQueryWrapper.eq("user_id", userId);
        UserTopicFollowing userTopicFollowing = userTopicFollowingService.getOne(userTopicFollowingQueryWrapper);
        if (userTopicFollowing == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        Topic topic = topicService.getById(topicId);
        ThrowUtils.throwIf(topic == null, ErrorCode.NOT_FOUND_ERROR);

        TopicVO topicVO = new TopicVO();
        topicVO.setId(topic.getId());
        topicVO.setTopicName(topic.getName());

        List<News> latestNewsList = new LinkedList<>();
        Long latestNews1Id = topic.getLatestNews1Id();
        Long latestNews2Id = topic.getLatestNews2Id();
        Long latestNews3Id = topic.getLatestNews3Id();
        // todo 这段if可以优化，因为latestNews1Id为空的话，后面的一定为空
        if (latestNews1Id != null) {
            News latestNews1 = newsService.getById(latestNews1Id);
            latestNewsList.add(latestNews1);
        }
        if (latestNews2Id != null) {
            News latestNews2 = newsService.getById(latestNews2Id);
            latestNewsList.add(latestNews2);
        }
        if (latestNews3Id != null) {
            News latestNews3 = newsService.getById(latestNews3Id);
            latestNewsList.add(latestNews3);
        }
        topicVO.setListOfNews(latestNewsList);

        topicVO.setNewContentTodayCount(topic.getNewContentTodayCount());
        return ResultUtils.success(topicVO);
    }

    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse<Page<Topic>> listFollowingTopicsByPage(@RequestBody UserTopicFollowingQueryRequest userTopicFollowingQueryRequest, HttpServletRequest request) {
        if (userTopicFollowingQueryRequest == null || userTopicFollowingQueryRequest.getUserId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        User user = userService.getLoginUser(request);
        Long operatorId = user.getId();
        Long userId = userTopicFollowingQueryRequest.getUserId();

        if (!userId.equals(operatorId) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        int current = userTopicFollowingQueryRequest.getCurrent();
        int size = userTopicFollowingQueryRequest.getPageSize();
        // 这里必须用包装类，因为topicId可能为null，但是我们允许topicId为null，所以不能在上面判空
        Long topicId = userTopicFollowingQueryRequest.getTopicId();

        QueryWrapper<UserTopicFollowing> userTopicFollowingQueryWrapper = new QueryWrapper<>();
        userTopicFollowingQueryWrapper.eq(topicId != null, "topic_id", topicId);
        userTopicFollowingQueryWrapper.eq(userId != null, "user_id", userId);
        Page<UserTopicFollowing> followingTopicIdsPage = userTopicFollowingService.page(new Page<>(current, size), userTopicFollowingQueryWrapper);
        List<UserTopicFollowing> userTopicFollowingList = followingTopicIdsPage.getRecords();
        List<Topic> topics = userTopicFollowingList.stream()
                .map(userTopicFollowing -> topicService.getById(userTopicFollowing.getTopicId()))
                .collect(Collectors.toList());

        Page<Topic> followingTopicsPage = new Page<>(current, size, followingTopicIdsPage.getTotal());
        followingTopicsPage.setRecords(topics);
        return ResultUtils.success(followingTopicsPage);
    }

    @PostMapping("/list/page/vo")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse<Page<TopicVO>> listFollowingTopicVOsByPage(@RequestBody UserTopicFollowingQueryRequest userTopicFollowingQueryRequest, HttpServletRequest request) {
        if (userTopicFollowingQueryRequest == null || userTopicFollowingQueryRequest.getUserId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        User user = userService.getLoginUser(request);
        Long operatorId = user.getId();
        Long userId = userTopicFollowingQueryRequest.getUserId();

        if (!userId.equals(operatorId) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        int current = userTopicFollowingQueryRequest.getCurrent();
        int size = userTopicFollowingQueryRequest.getPageSize();
        Long topicId = userTopicFollowingQueryRequest.getTopicId();

        QueryWrapper<UserTopicFollowing> userTopicFollowingQueryWrapper = new QueryWrapper<>();
        userTopicFollowingQueryWrapper.eq(topicId != null, "topic_id", topicId);
        userTopicFollowingQueryWrapper.eq("user_id", userId);
        Page<UserTopicFollowing> followingTopicIdsPage = userTopicFollowingService.page(new Page<>(current, size), userTopicFollowingQueryWrapper);

        List<UserTopicFollowing> userTopicFollowingList = followingTopicIdsPage.getRecords();
        List<Topic> topicList = userTopicFollowingList.stream()
                .map(userTopicFollowing -> topicService.getById(userTopicFollowing.getTopicId()))
                .collect(Collectors.toList());
        List<TopicVO> topicVOList = topicList.stream().sorted((t1, t2) -> {
            Long latestNews1IdOfO1 = t1.getLatestNews1Id();
            Long latestNews1IdOfO2 = t2.getLatestNews1Id();

            // 处理 null 情况
            if (latestNews1IdOfO1 == null && latestNews1IdOfO2 == null) {
                return 0; // 两者都为 null，视为相等
            }
            if (latestNews1IdOfO1 == null) {
                return 1; // t1 的 latestNews1Id 为 null，排在后面
            }
            if (latestNews1IdOfO2 == null) {
                return -1; // t2 的 latestNews1Id 为 null，排在后面
            }

            News newsOfO1 = newsService.getById(latestNews1IdOfO1);
            News newsOfO2 = newsService.getById(latestNews1IdOfO2);

            if (newsOfO1 == null || newsOfO2 == null) {
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
            }
            return newsOfO2.getUpdatedAt().compareTo(newsOfO1.getUpdatedAt());
        }).map(topic -> {
            TopicVO topicVO = new TopicVO();
            topicVO.setId(topic.getId());
            topicVO.setTopicName(topic.getName());
            topicVO.setNewContentTodayCount(topic.getNewContentTodayCount());
            List<News> latestNewsList = new LinkedList<>();
            Long latestNews1Id = topic.getLatestNews1Id();
            Long latestNews2Id = topic.getLatestNews2Id();
            Long latestNews3Id = topic.getLatestNews3Id();
            // todo 这段if可以优化，因为latestNews1Id为空的话，后面的一定为空
            if (latestNews1Id != null) {
                News latestNews1 = newsService.getById(latestNews1Id);
                latestNewsList.add(latestNews1);
            }
            if (latestNews2Id != null) {
                News latestNews2 = newsService.getById(latestNews2Id);
                latestNewsList.add(latestNews2);
            }
            if (latestNews3Id != null) {
                News latestNews3 = newsService.getById(latestNews3Id);
                latestNewsList.add(latestNews3);
            }
            topicVO.setListOfNews(latestNewsList);
            return topicVO;
        }).collect(Collectors.toList());


        Page<TopicVO> followingTopicVOsPage = new Page<>(current, size, followingTopicIdsPage.getTotal());
        followingTopicVOsPage.setRecords(topicVOList);
        return ResultUtils.success(followingTopicVOsPage);
    }
}

