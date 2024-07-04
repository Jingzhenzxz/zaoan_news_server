package com.zaoan.zaoan_news.zaoan_news_server.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zaoan.zaoan_news.zaoan_news_server.common.ErrorCode;
import com.zaoan.zaoan_news.zaoan_news_server.constant.CommonConstant;
import com.zaoan.zaoan_news.zaoan_news_server.exception.BusinessException;
import com.zaoan.zaoan_news.zaoan_news_server.exception.ThrowUtils;
import com.zaoan.zaoan_news.zaoan_news_server.mapper.NewsMapper;
import com.zaoan.zaoan_news.zaoan_news_server.model.dto.news.NewsQueryRequest;
import com.zaoan.zaoan_news.zaoan_news_server.model.entity.News;
import com.zaoan.zaoan_news.zaoan_news_server.model.entity.NewsTopic;
import com.zaoan.zaoan_news.zaoan_news_server.service.NewsService;
import com.zaoan.zaoan_news.zaoan_news_server.service.NewsTopicService;
import com.zaoan.zaoan_news.zaoan_news_server.util.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/07/30/ 13:35
 * @description
 */
@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {
    private final NewsTopicService newsTopicService;

    public NewsServiceImpl(NewsTopicService newsTopicService) {
        this.newsTopicService = newsTopicService;
    }

    @Override
    public QueryWrapper<News> getQueryWrapper(NewsQueryRequest newsQueryRequest) {
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
        Long topicId = newsQueryRequest.getTopicId();
        String sortField = newsQueryRequest.getSortField() == null ? "updated_at" : newsQueryRequest.getSortField();
        String sortOrder = newsQueryRequest.getSortOrder() == null ? "DESC" : newsQueryRequest.getSortOrder();

        QueryWrapper<News> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.eq(pubDate != null, "pub_date", pubDate);
        queryWrapper.like(StringUtils.isNotBlank(link), "link", link);
        queryWrapper.like(StringUtils.isNotBlank(author), "author", author);
        queryWrapper.like(StringUtils.isNotBlank(mediaName), "media_name", mediaName);
        queryWrapper.like(mediaId != null, "media_id", mediaId);

        List<Long> newsIdList = new LinkedList<>();
        if (topicId != null) {
            QueryWrapper<NewsTopic> newsTopicQueryWrapper = new QueryWrapper<>();
            newsTopicQueryWrapper.eq("topic_id", topicId);
            newsIdList = newsTopicService.list(newsTopicQueryWrapper).stream().map(NewsTopic::getNewsId).collect(Collectors.toList());
            // 只要topicId不为空，那么newsIdList一定不能为空
            ThrowUtils.throwIf(newsIdList.isEmpty(), ErrorCode.NOT_FOUND_ERROR);
        }

        // 只有在topicId为空的时候newsIdList才为空
        queryWrapper.in(!newsIdList.isEmpty(), "id", newsIdList);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }
}
