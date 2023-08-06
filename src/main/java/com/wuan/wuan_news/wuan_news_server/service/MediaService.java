package com.wuan.wuan_news.wuan_news_server.service;

import com.wuan.wuan_news.wuan_news_server.dto.MediaDTO;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/07/30/ 13:34
 * @description
 */
public interface MediaService {
    MediaDTO createMedia(String mediaName, String rssLink);

    MediaDTO deleteMediaByMediaName(String mediaName);
}
