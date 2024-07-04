package com.zaoan.zaoan_news.zaoan_news_server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zaoan.zaoan_news.zaoan_news_server.annotation.AuthCheck;
import com.zaoan.zaoan_news.zaoan_news_server.common.BaseResponse;
import com.zaoan.zaoan_news.zaoan_news_server.common.DeleteRequest;
import com.zaoan.zaoan_news.zaoan_news_server.common.ErrorCode;
import com.zaoan.zaoan_news.zaoan_news_server.common.ResultUtils;
import com.zaoan.zaoan_news.zaoan_news_server.constant.UserConstant;
import com.zaoan.zaoan_news.zaoan_news_server.exception.BusinessException;
import com.zaoan.zaoan_news.zaoan_news_server.exception.ThrowUtils;
import com.zaoan.zaoan_news.zaoan_news_server.model.dto.media.MediaAddRequest;
import com.zaoan.zaoan_news.zaoan_news_server.model.dto.media.MediaQueryRequest;
import com.zaoan.zaoan_news.zaoan_news_server.model.dto.media.MediaUpdateRequest;
import com.zaoan.zaoan_news.zaoan_news_server.model.entity.Media;
import com.zaoan.zaoan_news.zaoan_news_server.model.entity.User;
import com.zaoan.zaoan_news.zaoan_news_server.model.entity.UserMedia;
import com.zaoan.zaoan_news.zaoan_news_server.model.vo.MediaVO;
import com.zaoan.zaoan_news.zaoan_news_server.service.MediaService;
import com.zaoan.zaoan_news.zaoan_news_server.service.UserMediaService;
import com.zaoan.zaoan_news.zaoan_news_server.service.UserService;
import com.zaoan.zaoan_news.zaoan_news_server.task.NewsFetchTask;
import com.zaoan.zaoan_news.zaoan_news_server.util.CopyPropertiesUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/07/30/ 12:59
 * @description
 */

@RestController
@RequestMapping("/media")
public class MediaController {
    private final MediaService mediaService;
    private final UserService userService;
    private final UserMediaService userMediaService;
    private final NewsFetchTask newsFetchTask;

    public MediaController(MediaService mediaService, UserService userService, UserMediaService userMediaService, NewsFetchTask newsFetchTask) {
        this.mediaService = mediaService;
        this.userService = userService;
        this.userMediaService = userMediaService;
        this.newsFetchTask = newsFetchTask;
    }

    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse<Boolean> createMedia(@RequestBody MediaAddRequest mediaAddRequest, HttpServletRequest request) {
        if (mediaAddRequest == null || StringUtils.isAnyBlank(mediaAddRequest.getName(), mediaAddRequest.getRssLink())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 先创建media
        Media media = new Media();
        BeanUtils.copyProperties(mediaAddRequest, media);
        boolean mediaSaved = mediaService.save(media);
        ThrowUtils.throwIf(!mediaSaved, ErrorCode.OPERATION_ERROR);

        // 再创建UserMedia
        User user = userService.getLoginUser(request);
        long userId = user.getId();
        QueryWrapper<Media> mediaQueryWrapper = new QueryWrapper<>();
        mediaQueryWrapper.eq("name", media.getName());
        mediaQueryWrapper.eq("rss_link", media.getRssLink());

        long mediaId = mediaService.getOne(mediaQueryWrapper).getId();
        UserMedia userMedia = new UserMedia();
        userMedia.setUserId(userId);
        userMedia.setMediaId(mediaId);

        boolean userMediaSaved = userMediaService.save(userMedia);
        ThrowUtils.throwIf(!userMediaSaved, ErrorCode.OPERATION_ERROR);

        // 创建完媒体后立刻获取新闻
        newsFetchTask.fetchNewsFromRss(mediaService.getById(mediaId));
        return ResultUtils.success(true);
    }

    @DeleteMapping("/delete")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse<Boolean> deleteMedia(
            @RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        long mediaId = deleteRequest.getId();

        // 先检查权限
        Media media = mediaService.getById(mediaId);
        if (media == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        User user = userService.getLoginUser(request);
        Long operatorId = user.getId();
        QueryWrapper<UserMedia> userMediaQueryWrapper = new QueryWrapper<>();
        userMediaQueryWrapper.eq("media_id", mediaId);
        userMediaQueryWrapper.eq("user_id", operatorId);
        UserMedia userMedia = userMediaService.getOne(userMediaQueryWrapper);
        // 仅本人或管理员可以删除
        if (userMedia == null || !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        // 如果有权限，则进行删除，先删除userMedia，再删除media，不然会受到外键约束
        boolean userMediaDeleted = userMediaService.remove(userMediaQueryWrapper);
        ThrowUtils.throwIf(!userMediaDeleted, ErrorCode.OPERATION_ERROR);

        boolean mediaDeleted = mediaService.removeById(mediaId);
        ThrowUtils.throwIf(!mediaDeleted, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse<Boolean> updateMedia(@RequestBody MediaUpdateRequest mediaUpdateRequest, HttpServletRequest request) {
        if (mediaUpdateRequest == null || mediaUpdateRequest.getId() == null || mediaUpdateRequest.getUserId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 检查权限
        User user = userService.getLoginUser(request);
        Long operatorId = user.getId();
        Long userId = mediaUpdateRequest.getUserId();
        if (!operatorId.equals(userId) || !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        // 开始更新
        Long id = mediaUpdateRequest.getId();
        String name = mediaUpdateRequest.getName();
        String rssLink = mediaUpdateRequest.getRssLink();
        Media oldMedia = mediaService.getById(id);
        if (StringUtils.isNotBlank(name)) {
            oldMedia.setName(name);
        }
        if (StringUtils.isNotBlank(rssLink)) {
            oldMedia.setRssLink(rssLink);
        }

        boolean mediaUpdated = mediaService.updateById(oldMedia);
        ThrowUtils.throwIf(!mediaUpdated, ErrorCode.OPERATION_ERROR);

        // 更新完媒体后立刻获取新闻
        newsFetchTask.fetchNewsFromRss(oldMedia);
        return ResultUtils.success(true);
    }

    @GetMapping("/getById")
    public BaseResponse<Media> getMediaById(@RequestParam Long id, HttpServletRequest request) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Media media = mediaService.getById(id);
        ThrowUtils.throwIf(media == null, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(media);
    }

    @PostMapping("/getByNameAndLink")
    public BaseResponse<Media> getMediaByNameAndLink(@RequestBody MediaQueryRequest mediaQueryRequest, HttpServletRequest request) {
        if (mediaQueryRequest.getName() == null || mediaQueryRequest.getRssLink() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String mediaName = mediaQueryRequest.getName();
        String rssLink = mediaQueryRequest.getRssLink();

        QueryWrapper<Media> mediaQueryWrapper = new QueryWrapper<>();
        mediaQueryWrapper.eq("name", mediaName);
        mediaQueryWrapper.eq("rss_link", rssLink);
        Media media = mediaService.getOne(mediaQueryWrapper);
        ThrowUtils.throwIf(media == null, ErrorCode.OPERATION_ERROR);

        return ResultUtils.success(media);
    }

    @GetMapping("/getById/vo")
    public BaseResponse<MediaVO> getMediaVOById(@RequestParam Long id, HttpServletRequest request) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Media media = mediaService.getById(id);
        ThrowUtils.throwIf(media == null, ErrorCode.OPERATION_ERROR);
        MediaVO mediaVO = new MediaVO();
        BeanUtils.copyProperties(media, mediaVO);
        return ResultUtils.success(mediaVO);
    }

    @PostMapping("/getByNameAndLink/vo")
    public BaseResponse<MediaVO> getMediaVOByNameAndLink(@RequestBody MediaQueryRequest mediaQueryRequest, HttpServletRequest request) {
        if (mediaQueryRequest.getName() == null || mediaQueryRequest.getRssLink() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String mediaName = mediaQueryRequest.getName();
        String rssLink = mediaQueryRequest.getRssLink();

        QueryWrapper<Media> mediaQueryWrapper = new QueryWrapper<>();
        mediaQueryWrapper.eq("name", mediaName);
        mediaQueryWrapper.eq("rss_link", rssLink);
        Media media = mediaService.getOne(mediaQueryWrapper);
        ThrowUtils.throwIf(media == null, ErrorCode.OPERATION_ERROR);

        MediaVO mediaVO = new MediaVO();
        BeanUtils.copyProperties(media, mediaVO);
        return ResultUtils.success(mediaVO);
    }

    @PostMapping("/list/page")
    public BaseResponse<Page<Media>> listMediaByPage(@RequestBody MediaQueryRequest mediaQueryRequest, HttpServletRequest request) {
        long current = mediaQueryRequest.getCurrent();
        long size = mediaQueryRequest.getPageSize();
        Page<Media> mediaPage = mediaService.page(new Page<>(current, size),
                mediaService.getQueryWrapper(mediaQueryRequest));
        return ResultUtils.success(mediaPage);
    }
}
