package com.wuan.wuan_news.wuan_news_server.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wuan.wuan_news.wuan_news_server.model.dto.media.MediaQueryRequest;
import com.wuan.wuan_news.wuan_news_server.model.entity.Media;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/07/30/ 13:34
 * @description
 */
public interface MediaService extends IService<Media> {
    Wrapper<Media> getQueryWrapper(MediaQueryRequest mediaQueryRequest);
}
