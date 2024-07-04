package com.zaoan.zaoan_news.zaoan_news_server.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zaoan.zaoan_news.zaoan_news_server.common.ErrorCode;
import com.zaoan.zaoan_news.zaoan_news_server.constant.CommonConstant;
import com.zaoan.zaoan_news.zaoan_news_server.exception.BusinessException;
import com.zaoan.zaoan_news.zaoan_news_server.mapper.MediaMapper;
import com.zaoan.zaoan_news.zaoan_news_server.model.dto.media.MediaQueryRequest;
import com.zaoan.zaoan_news.zaoan_news_server.model.entity.Media;
import com.zaoan.zaoan_news.zaoan_news_server.model.entity.UserMedia;
import com.zaoan.zaoan_news.zaoan_news_server.service.MediaService;
import com.zaoan.zaoan_news.zaoan_news_server.service.UserMediaService;
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
 * @date 2023/07/30/ 13:36
 * @description
 */
@Service
public class MediaServiceImpl extends ServiceImpl<MediaMapper, Media> implements MediaService {
    private final UserMediaService userMediaService;

    public MediaServiceImpl(UserMediaService userMediaService) {
        this.userMediaService = userMediaService;
    }

    @Override
    public Wrapper<Media> getQueryWrapper(MediaQueryRequest mediaQueryRequest) {
        if (mediaQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = mediaQueryRequest.getId();
        String mediaName = mediaQueryRequest.getName();
        String rssLink = mediaQueryRequest.getRssLink();
        Long userId = mediaQueryRequest.getUserId();
        String sortField = mediaQueryRequest.getSortField();
        String sortOrder = mediaQueryRequest.getSortOrder();

        List<Long> mediaIdList = new ArrayList<>();
        if (userId != null) {
            QueryWrapper<UserMedia> userMediaWrapper = new QueryWrapper<>();
            userMediaWrapper.eq("user_id", userId);
            mediaIdList = userMediaService.list(userMediaWrapper).stream().map(UserMedia::getMediaId).collect(Collectors.toList());
        }
        QueryWrapper<Media> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.in(!mediaIdList.isEmpty(), "id", mediaIdList);
        queryWrapper.eq(StringUtils.isNotBlank(mediaName), "name", mediaName);
        queryWrapper.eq(StringUtils.isNotBlank(rssLink), "rss_link", rssLink);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }
}
