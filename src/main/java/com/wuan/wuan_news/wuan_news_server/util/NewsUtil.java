package com.wuan.wuan_news.wuan_news_server.util;

import com.wuan.wuan_news.wuan_news_server.dto.NewsDTO;
import com.wuan.wuan_news.wuan_news_server.model.News;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/08/05/ 11:17
 * @description
 */
@Service
public class NewsUtil {
    public NewsDTO convertNewsModelToNewsDTO(News news) {
        // Convert the User object to UserDto object
        if (news == null) {
            return null;
        }
        NewsDTO newsDTO = new NewsDTO();
        newsDTO.setTitle(news.getTitle());
        newsDTO.setDescription(news.getDescription());
        newsDTO.setAuthor(news.getAuthor());
        newsDTO.setLink(news.getLink());
        newsDTO.setPreviewImage(news.getPreviewImage());
        newsDTO.setPubDate(news.getPubDate());
        newsDTO.setMediaName(news.getMediaName());
        newsDTO.setCreatedAt(news.getCreatedAt());
        newsDTO.setUpdatedAt(news.getUpdatedAt());

        return newsDTO;
    }

    public News convertNewsDTOToNewsModel(NewsDTO newsDTO) {
        // Convert the UserDto object to User object
        if (newsDTO == null) {
            return null;
        }

        News news = new News();
        news.setAuthor(newsDTO.getAuthor());
        news.setDescription(newsDTO.getDescription());
        news.setLink(newsDTO.getLink());
        news.setMediaName(newsDTO.getMediaName());
        news.setPreviewImage(newsDTO.getPreviewImage());
        news.setTitle(newsDTO.getTitle());
        news.setPubDate(newsDTO.getPubDate());
        news.setCreatedAt(newsDTO.getCreatedAt());
        news.setUpdatedAt(newsDTO.getUpdatedAt());

        return news;
    }
}
