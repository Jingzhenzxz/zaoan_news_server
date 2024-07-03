package com.wuan.wuan_news.wuan_news_server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuan.wuan_news.wuan_news_server.annotation.AuthCheck;
import com.wuan.wuan_news.wuan_news_server.common.BaseResponse;
import com.wuan.wuan_news.wuan_news_server.common.DeleteRequest;
import com.wuan.wuan_news.wuan_news_server.common.ErrorCode;
import com.wuan.wuan_news.wuan_news_server.common.ResultUtils;
import com.wuan.wuan_news.wuan_news_server.constant.UserConstant;
import com.wuan.wuan_news.wuan_news_server.exception.BusinessException;
import com.wuan.wuan_news.wuan_news_server.exception.ThrowUtils;
import com.wuan.wuan_news.wuan_news_server.model.dto.topic.TopicAddRequest;
import com.wuan.wuan_news.wuan_news_server.model.dto.topic.TopicQueryRequest;
import com.wuan.wuan_news.wuan_news_server.model.dto.topic.TopicUpdateRequest;
import com.wuan.wuan_news.wuan_news_server.model.entity.*;
import com.wuan.wuan_news.wuan_news_server.model.vo.TopicVO;
import com.wuan.wuan_news.wuan_news_server.service.*;
import com.wuan.wuan_news.wuan_news_server.util.UpdateLatestNewsOfTopicUtils;
import io.swagger.annotations.ApiParam;
import net.sf.jsqlparser.statement.select.Top;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/09/02/ 9:54
 * @description
 */
@RestController
@RequestMapping("/topic")
public class TopicController {
    private final TopicService topicService;
    private final UserService userService;
    private final NewsService newsService;
    private final NewsTopicService newsTopicService;
    private final UserTopicFollowingService userTopicFollowingService;
    private final UpdateLatestNewsOfTopicUtils updateLatestNewsOfTopicUtils;

    @Autowired
    public TopicController(TopicService topicService, UserService userService, NewsService newsService, NewsTopicService newsTopicService, UserTopicFollowingService userTopicFollowingService, UpdateLatestNewsOfTopicUtils updateLatestNewsOfTopicUtils) {
        this.topicService = topicService;
        this.userService = userService;
        this.newsService = newsService;
        this.newsTopicService = newsTopicService;
        this.userTopicFollowingService = userTopicFollowingService;
        this.updateLatestNewsOfTopicUtils = updateLatestNewsOfTopicUtils;
    }

    /**
     * 创建话题
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse<Long> addTopic(
            @ApiParam(value = "Topic creation request object", required = true)
            @Valid @RequestBody TopicAddRequest topicAddRequest,
            HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        Topic topic = findTopicByName(topicAddRequest.getName());

        if (topic == null) {
            return createNewTopic(topicAddRequest, user);
        } else {
            throw new BusinessException(ErrorCode.DATA_ALREADY_EXISTS);
        }
    }

    private Topic findTopicByName(String name) {
        QueryWrapper<Topic> topicQueryWrapper = new QueryWrapper<>();
        topicQueryWrapper.eq("name", name);
        return topicService.getOne(topicQueryWrapper);
    }

    private BaseResponse<Long> createNewTopic(TopicAddRequest topicAddRequest, User user) {
        Topic newTopic = new Topic();
        newTopic.setName(topicAddRequest.getName());
        newTopic.setUserId(user.getId());
        // save完后newTopic自动获得id，此时可以通过newTopic.getId()获得它的id
        boolean saved = topicService.save(newTopic);

        if (!saved) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }

        return associateTopicWithUserAndNews(newTopic, user, topicAddRequest);
    }

    // private BaseResponse<Boolean> handleExistingTopic(Topic topic, User user) {
    //     if (isUserAlreadyAssociatedWithTopic(topic, user)) {
    //         throw new BusinessException(ErrorCode.DATA_ALREADY_EXISTS);
    //     }
    //
    //     UserTopicFollowing newUserTopicFollowing = new UserTopicFollowing();
    //     newUserTopicFollowing.setTopicId(topic.getId());
    //     newUserTopicFollowing.setUserId(user.getId());
    //     boolean userTopicSaved = userTopicFollowingService.save(newUserTopicFollowing);
    //
    //     if (userTopicSaved) {
    //         return ResultUtils.success(true);
    //     } else {
    //         throw new BusinessException(ErrorCode.OPERATION_ERROR);
    //     }
    // }
    //
    // private boolean isUserAlreadyAssociatedWithTopic(Topic topic, User user) {
    //     QueryWrapper<UserTopicFollowing> userTopicFollowingQueryWrapper = new QueryWrapper<>();
    //     userTopicFollowingQueryWrapper.eq("topic_id", topic.getId()).eq("user_id", user.getId());
    //     UserTopicFollowing userTopicFollowing = userTopicFollowingService.getOne(userTopicFollowingQueryWrapper);
    //     return userTopicFollowing != null;
    // }

    private BaseResponse<Long> associateTopicWithUserAndNews(Topic topic, User user, TopicAddRequest topicAddRequest) {
        UserTopicFollowing userTopicFollowing = new UserTopicFollowing();
        userTopicFollowing.setTopicId(topic.getId());
        userTopicFollowing.setUserId(user.getId());
        boolean userTopicFollowingSaved = userTopicFollowingService.save(userTopicFollowing);

        if (!userTopicFollowingSaved) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }

        if (!associateNewsWithTopic(topic, topicAddRequest)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }

        return ResultUtils.success(topic.getId());
    }

    private boolean associateNewsWithTopic(Topic topic, TopicAddRequest topicAddRequest) {
        QueryWrapper<News> newsQueryWrapper = new QueryWrapper<>();
        newsQueryWrapper.like(StringUtils.isNotBlank(topicAddRequest.getName()), "title", topicAddRequest.getName());
        List<News> newsList = newsService.list(newsQueryWrapper);
        for (News news : newsList) {
            NewsTopic newsTopic = new NewsTopic();
            newsTopic.setNewsId(news.getId());
            newsTopic.setTopicId(topic.getId());
            if (!newsTopicService.save(newsTopic)) {
                return false;
            }
        }
        // 确定该话题的前三条最新新闻，并更新今日新闻数
        Set<Topic> topicSet = new HashSet<>();
        topicSet.add(topic);
        updateLatestNewsOfTopicUtils.updateLatestThreeNewsOfTopic(topicSet);
        return true;
    }

    /**
     * 删除话题
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse<Boolean> deleteTopic(
            @ApiParam(value = "Topic deletion request object", required = true)
            @Valid @RequestBody DeleteRequest deleteRequest,
            HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        long topicId = deleteRequest.getId();

        // 判断是否存在
        Topic oldTopic = topicService.getById(topicId);
        ThrowUtils.throwIf(oldTopic == null, ErrorCode.NOT_FOUND_ERROR);

        // 检查权限
        User user = userService.getLoginUser(request);
        long operatorId = user.getId();
        QueryWrapper<Topic> topicQueryWrapper = new QueryWrapper<>();
        topicQueryWrapper.eq("id", topicId);
        topicQueryWrapper.eq("user_id", operatorId);
        Topic topic = topicService.getOne(topicQueryWrapper);
        // 话题不属于当前用户且当前用户不是管理员
        if (topic == null && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        // 执行删除操作，先删除newsTopic，再删除userTopicFollowing，再删除topic
        QueryWrapper<NewsTopic> newsTopicQueryWrapper = new QueryWrapper<>();
        newsTopicQueryWrapper.eq("topic_id", topicId);
        // 查询匹配的记录数量
        long countOfNewsTopic = newsTopicService.count(newsTopicQueryWrapper);
        if (countOfNewsTopic > 0) {
            boolean newsTopicRemoved = newsTopicService.remove(newsTopicQueryWrapper);
            ThrowUtils.throwIf(!newsTopicRemoved, ErrorCode.OPERATION_ERROR);
        }

        QueryWrapper<UserTopicFollowing> userTopicFollowingQueryWrapper = new QueryWrapper<>();
        userTopicFollowingQueryWrapper.eq("topic_id", topicId);
        // 查询匹配的记录数量
        long countOfUserTopicFollowing = userTopicFollowingService.count(userTopicFollowingQueryWrapper);
        if (countOfUserTopicFollowing > 0) {
            boolean userTopicFollowingRemoved = userTopicFollowingService.remove(userTopicFollowingQueryWrapper);
            ThrowUtils.throwIf(!userTopicFollowingRemoved, ErrorCode.OPERATION_ERROR);
        }

        boolean topicRemoved = topicService.removeById(topicId);
        ThrowUtils.throwIf(!topicRemoved, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse<Boolean> updateTopic(
            @ApiParam(value = "Topic deletion request object", required = true)
            @Valid @RequestBody TopicUpdateRequest topicUpdateRequest,
            HttpServletRequest request) {
        if (topicUpdateRequest == null || topicUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        Long operatorId = user.getId();
        long topicId = topicUpdateRequest.getId();
        // 判断是否存在
        QueryWrapper<UserTopicFollowing> userTopicQueryWrapper = new QueryWrapper<>();
        userTopicQueryWrapper.eq("topic_id", topicId);
        userTopicQueryWrapper.eq("user_id", operatorId);
        UserTopicFollowing userTopicFollowing = userTopicFollowingService.getOne(userTopicQueryWrapper);
        ThrowUtils.throwIf(userTopicFollowing == null, ErrorCode.NOT_FOUND_ERROR);

        // 仅本人或管理员可更新
        Long userId = topicUpdateRequest.getUserId();
        if (!userId.equals(operatorId) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        Topic topic = new Topic();
        BeanUtils.copyProperties(topicUpdateRequest, topic);
        boolean updated = topicService.updateById(topic);
        ThrowUtils.throwIf(!updated, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    @GetMapping("/getById")
    public BaseResponse<Topic> getTopicById(@RequestParam Long id, HttpServletRequest request) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Topic topic = topicService.getById(id);
        ThrowUtils.throwIf(topic == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(topic);
    }

    @GetMapping("/getByName")
    public BaseResponse<Topic> getTopicByName(@RequestParam String topicName, HttpServletRequest request) {
        if (topicName == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<Topic> topicQueryWrapper = new QueryWrapper<>();
        topicQueryWrapper.eq("name", topicName);
        Topic topic = topicService.getOne(topicQueryWrapper);
        ThrowUtils.throwIf(topic == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(topic);
    }

    @GetMapping("/getById/vo")
    public BaseResponse<TopicVO> getTopicVOById(@RequestParam Long id, HttpServletRequest request) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Topic topic = topicService.getById(id);
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

    @GetMapping("/getByName/vo")
    public BaseResponse<TopicVO> getTopicVOByName(@RequestParam String topicName, HttpServletRequest request) {
        if (topicName == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<Topic> topicQueryWrapper = new QueryWrapper<>();
        topicQueryWrapper.eq("name", topicName);
        Topic topic = topicService.getOne(topicQueryWrapper);
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
    public BaseResponse<Page<Topic>> listTopicByPage(@RequestBody TopicQueryRequest topicQueryRequest, HttpServletRequest request) {
        long current = topicQueryRequest.getCurrent();
        long size = topicQueryRequest.getPageSize();
        Page<Topic> topicPage = topicService.page(new Page<>(current, size),
                topicService.getQueryWrapper(topicQueryRequest));
        List<Topic> topicList = topicPage.getRecords();
        List<Topic> sortedTopicList = topicList.stream().sorted((t1, t2) -> {
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
        }).collect(Collectors.toList());
        Page<Topic> sortedTopicPage = new Page<>(current, size, topicPage.getTotal());
        sortedTopicPage.setRecords(sortedTopicList);
        return ResultUtils.success(sortedTopicPage);
    }

    @PostMapping("/list/page/vo")
    public BaseResponse<Page<TopicVO>> listTopicVOByPage(@RequestBody TopicQueryRequest topicQueryRequest, HttpServletRequest request) {
        long current = topicQueryRequest.getCurrent();
        long size = topicQueryRequest.getPageSize();
        Page<Topic> topicPage = topicService.page(new Page<>(current, size),
                topicService.getQueryWrapper(topicQueryRequest));
        List<Topic> topicList = topicPage.getRecords();
        List<TopicVO> topicVOList = topicList.stream()
                .sorted((t1, t2) -> {
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
                })
                .map(topic -> {
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
                })
                .collect(Collectors.toList());

        Page<TopicVO> topicVOPage = new Page<>(current, size, topicPage.getTotal());
        topicVOPage.setRecords(topicVOList);
        return ResultUtils.success(topicVOPage);
    }
}
