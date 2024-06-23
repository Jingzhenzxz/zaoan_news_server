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
import com.wuan.wuan_news.wuan_news_server.service.*;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

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
    private final UserTopicService userTopicService;

    @Autowired
    public TopicController(TopicService topicService, UserService userService, NewsService newsService, NewsTopicService newsTopicService, UserTopicService userTopicService) {
        this.topicService = topicService;
        this.userService = userService;
        this.newsService = newsService;
        this.newsTopicService = newsTopicService;
        this.userTopicService = userTopicService;
    }

    /**
     * 创建话题
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse addTopic(
            @ApiParam(value = "Topic creation request object", required = true)
            @Valid @RequestBody TopicAddRequest topicAddRequest,
            HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        Topic topic = findTopicByName(topicAddRequest.getName());

        if (topic == null) {
            return createNewTopic(topicAddRequest, user);
        } else {
            return handleExistingTopic(topic, user);
        }
    }

    private Topic findTopicByName(String name) {
        QueryWrapper<Topic> topicQueryWrapper = new QueryWrapper<>();
        topicQueryWrapper.eq("name", name);
        return topicService.getOne(topicQueryWrapper);
    }

    private BaseResponse createNewTopic(TopicAddRequest topicAddRequest, User user) {
        Topic newTopic = new Topic();
        newTopic.setName(topicAddRequest.getName());
        boolean saved = topicService.save(newTopic);

        if (!saved) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "话题保存失败");
        }

        return associateTopicWithUserAndNews(newTopic, user, topicAddRequest);
    }

    private BaseResponse handleExistingTopic(Topic topic, User user) {
        if (isUserAlreadyAssociatedWithTopic(topic, user)) {
            return ResultUtils.error(ErrorCode.DATA_ALREADY_EXISTS, "用户已关联该话题");
        }

        UserTopic newUserTopic = new UserTopic();
        newUserTopic.setTopicId(topic.getId());
        newUserTopic.setUserId(user.getId());
        boolean userTopicSaved = userTopicService.save(newUserTopic);

        if (userTopicSaved) {
            return ResultUtils.success("创建话题成功！");
        } else {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "关联用户话题失败");
        }
    }

    private boolean isUserAlreadyAssociatedWithTopic(Topic topic, User user) {
        QueryWrapper<UserTopic> userTopicQueryWrapper = new QueryWrapper<>();
        userTopicQueryWrapper.eq("topic_id", topic.getId()).eq("user_id", user.getId());
        UserTopic userTopic = userTopicService.getOne(userTopicQueryWrapper);
        return userTopic != null;
    }

    private BaseResponse associateTopicWithUserAndNews(Topic topic, User user, TopicAddRequest topicAddRequest) {
        UserTopic userTopic = new UserTopic();
        userTopic.setTopicId(topic.getId());
        userTopic.setUserId(user.getId());
        boolean userTopicSaved = userTopicService.save(userTopic);

        if (!userTopicSaved) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "用户话题关联失败");
        }

        if (!associateNewsWithTopic(topic, topicAddRequest)) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "新闻话题关联失败");
        }

        return ResultUtils.success("创建话题成功！");
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
        return true;
    }

    /**
     * 删除话题
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse deleteTopic(
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
        QueryWrapper<UserTopic> userTopicQueryWrapper = new QueryWrapper<>();
        userTopicQueryWrapper.eq("topic_id", topicId);
        userTopicQueryWrapper.eq("user_id", operatorId);
        UserTopic userTopic = userTopicService.getOne(userTopicQueryWrapper);
        if (userTopic == null && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        // 执行删除操作，先删除topic，再删除newsTopic，再删除userTopic
        boolean topicRemoved = topicService.removeById(topicId);
        ThrowUtils.throwIf(!topicRemoved, ErrorCode.OPERATION_ERROR);
        QueryWrapper<NewsTopic> newsTopicQueryWrapper = new QueryWrapper<>();
        newsTopicQueryWrapper.eq("topic_id", topicId);
        boolean newsTopicRemoved = newsTopicService.remove(newsTopicQueryWrapper);
        ThrowUtils.throwIf(!newsTopicRemoved, ErrorCode.OPERATION_ERROR);
        boolean userTopicRemoved = userTopicService.remove(userTopicQueryWrapper);
        ThrowUtils.throwIf(!userTopicRemoved, ErrorCode.OPERATION_ERROR);

        return ResultUtils.success(true);
    }

    @PostMapping("/update")
    public BaseResponse updateTopic(
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
        QueryWrapper<UserTopic> userTopicQueryWrapper = new QueryWrapper<>();
        userTopicQueryWrapper.eq("topic_id", topicId);
        userTopicQueryWrapper.eq("user_id", operatorId);
        UserTopic userTopic = userTopicService.getOne(userTopicQueryWrapper);
        ThrowUtils.throwIf(userTopic == null, ErrorCode.NOT_FOUND_ERROR);

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

    @GetMapping("/get")
    public BaseResponse getTopicById(@RequestBody Long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Topic topic = topicService.getById(id);
        ThrowUtils.throwIf(topic == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(topic);
    }

    @PostMapping("/list/page")
    public BaseResponse<Page<Topic>> listTopicByPage(@RequestBody TopicQueryRequest topicQueryRequest, HttpServletRequest request) {
        long current = topicQueryRequest.getCurrent();
        long size = topicQueryRequest.getPageSize();
        Page<Topic> topicPage = topicService.page(new Page<>(current, size),
                topicService.getQueryWrapper(topicQueryRequest));

        return ResultUtils.success(topicPage);
    }
}
