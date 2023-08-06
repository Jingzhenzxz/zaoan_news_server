package com.wuan.wuan_news.wuan_news_server.service.impl;

import com.wuan.wuan_news.wuan_news_server.dto.MediaDTO;
import com.wuan.wuan_news.wuan_news_server.exception.InvalidRSSFormatException;
import com.wuan.wuan_news.wuan_news_server.exception.MediaCreationException;
import com.wuan.wuan_news.wuan_news_server.mapper.MediaMapper;
import com.wuan.wuan_news.wuan_news_server.service.MediaService;
import com.wuan.wuan_news.wuan_news_server.util.MediaUtil;
import com.wuan.wuan_news.wuan_news_server.util.RssUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/07/30/ 13:36
 * @description
 */
@Service
public class MediaServiceImpl implements MediaService {
    private final MediaMapper mediaMapper;
    private final RssUtil rssUtil;
    private final MediaUtil mediaUtil;
    @Autowired
    public MediaServiceImpl(MediaMapper mediaMapper, RssUtil rssUtil, MediaUtil mediaUtil) {
        this.mediaMapper = mediaMapper;
        this.rssUtil = rssUtil;
        this.mediaUtil = mediaUtil;
    }

    @Override
    public MediaDTO createMedia(MediaDTO mediaDTO) {
        if (!rssUtil.isValidRssUrl(mediaDTO.getRssLink())) {
            throw new InvalidRSSFormatException("RSS 链接的格式有误");
        }

        Integer result = mediaMapper.insert(mediaUtil.convertMediaDTOToMediaModel(mediaDTO));
        if (result == 0) {
            throw new MediaCreationException("创建媒体失败");
        } else {
            return mediaUtil.convertMediaModelToMediaDTO(mediaMapper.findByName(mediaDTO.getName()));
        }
    }
}
