package com.wuan.wuan_news.wuan_news_server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuan.wuan_news.wuan_news_server.annotation.AuthCheck;
import com.wuan.wuan_news.wuan_news_server.common.*;
import com.wuan.wuan_news.wuan_news_server.constant.UserConstant;
import com.wuan.wuan_news.wuan_news_server.exception.BusinessException;
import com.wuan.wuan_news.wuan_news_server.exception.ThrowUtils;
import com.wuan.wuan_news.wuan_news_server.model.dto.media.MediaAddRequest;
import com.wuan.wuan_news.wuan_news_server.model.dto.media.MediaQueryRequest;
import com.wuan.wuan_news.wuan_news_server.model.dto.media.MediaUpdateRequest;
import com.wuan.wuan_news.wuan_news_server.model.entity.Media;
import com.wuan.wuan_news.wuan_news_server.model.entity.User;
import com.wuan.wuan_news.wuan_news_server.model.entity.UserMedia;
import com.wuan.wuan_news.wuan_news_server.service.MediaService;
import com.wuan.wuan_news.wuan_news_server.service.UserMediaService;
import com.wuan.wuan_news.wuan_news_server.service.UserService;
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

    public MediaController(MediaService mediaService, UserService userService, UserMediaService userMediaService) {
        this.mediaService = mediaService;
        this.userService = userService;
        this.userMediaService = userMediaService;
    }

    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse createMedia(@RequestBody MediaAddRequest mediaAddRequest, HttpServletRequest request) {
        if (mediaAddRequest == null || StringUtils.isAnyBlank(mediaAddRequest.getName(), mediaAddRequest.getRssLink(), mediaAddRequest.getUserId().toString())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 先创建media
        Media media = new Media();
        BeanUtils.copyProperties(mediaAddRequest, media);
        boolean mediaSaved = mediaService.save(media);
        ThrowUtils.throwIf(!mediaSaved, ErrorCode.OPERATION_ERROR);

        // 再创建UserMedia
        long userId = mediaAddRequest.getUserId();
        QueryWrapper<Media> mediaQueryWrapper = new QueryWrapper<>();
        mediaQueryWrapper.eq("name", media.getName());
        mediaQueryWrapper.eq("rss_link", media.getRssLink());
        long mediaId = mediaService.getOne(mediaQueryWrapper).getId();
        UserMedia userMedia = new UserMedia();
        userMedia.setUserId(userId);
        userMedia.setMediaId(mediaId);
        boolean userMediaSaved = userMediaService.save(userMedia);
        ThrowUtils.throwIf(!userMediaSaved, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    @DeleteMapping("/delete")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse deleteMedia(
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
        userMediaQueryWrapper.eq("userId", operatorId);
        UserMedia userMedia = userMediaService.getOne(userMediaQueryWrapper);
        // 仅本人或管理员可以删除
        if (userMedia == null || !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        // 如果有权限，则进行删除，先删除media，再删除userMedia
        boolean mediaDeleted = mediaService.removeById(mediaId);
        ThrowUtils.throwIf(!mediaDeleted, ErrorCode.OPERATION_ERROR);
        boolean userMediaDeleted = userMediaService.removeById(userMedia);
        ThrowUtils.throwIf(!userMediaDeleted, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse updateMedia(@RequestBody MediaUpdateRequest mediaUpdateRequest, HttpServletRequest request) {
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
        Media media = new Media();
        BeanUtils.copyProperties(mediaUpdateRequest, media);
        boolean mediaSaved = mediaService.updateById(media);
        ThrowUtils.throwIf(!mediaSaved, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    @GetMapping("/get")
    public BaseResponse getMediaById(Long id, HttpServletRequest request) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Media media = mediaService.getById(id);
        ThrowUtils.throwIf(media == null, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(media);
    }

    @PostMapping("/list/page")
    public BaseResponse listMediaByPage(@RequestBody MediaQueryRequest mediaQueryRequest, HttpServletRequest request) {
        long current = mediaQueryRequest.getCurrent();
        long size = mediaQueryRequest.getPageSize();
        Page<Media> mediaPage = mediaService.page(new Page<>(current, size),
                mediaService.getQueryWrapper(mediaQueryRequest));
        return ResultUtils.success(mediaPage);
    }
}
