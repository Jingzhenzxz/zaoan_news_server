package com.wuan.wuan_news.wuan_news_server.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuan.wuan_news.wuan_news_server.common.ErrorCode;
import com.wuan.wuan_news.wuan_news_server.constant.CommonConstant;
import com.wuan.wuan_news.wuan_news_server.exception.BusinessException;
import com.wuan.wuan_news.wuan_news_server.mapper.NewsMapper;
import com.wuan.wuan_news.wuan_news_server.model.dto.news.NewsQueryRequest;
import com.wuan.wuan_news.wuan_news_server.model.entity.News;
import com.wuan.wuan_news.wuan_news_server.service.NewsService;
import com.wuan.wuan_news.wuan_news_server.util.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/07/30/ 13:35
 * @description
 */
@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {
    @Override
    public Wrapper<News> getQueryWrapper(NewsQueryRequest newsQueryRequest) {
        if (newsQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = newsQueryRequest.getId();
        String title = newsQueryRequest.getTitle();
        LocalDateTime pubDate = newsQueryRequest.getPubDate();
        String link = newsQueryRequest.getLink();
        String author = newsQueryRequest.getAuthor();
        String mediaName = newsQueryRequest.getMediaName();
        Long mediaId = newsQueryRequest.getMediaId();
        String sortField = newsQueryRequest.getSortField();
        String sortOrder = newsQueryRequest.getSortOrder();
        QueryWrapper<News> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.eq(pubDate != null, "pub_date", pubDate);
        queryWrapper.like(StringUtils.isNotBlank(link), "link", link);
        queryWrapper.like(StringUtils.isNotBlank(author), "author", author);
        queryWrapper.like(StringUtils.isNotBlank(mediaName), "media_name", mediaName);
        queryWrapper.like(mediaId != null, "media_id", mediaId);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }
}
