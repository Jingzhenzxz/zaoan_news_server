package com.wuan.wuan_news.wuan_news_server.service.impl;

import com.wuan.wuan_news.wuan_news_server.dto.MediaDTO;
import com.wuan.wuan_news.wuan_news_server.exception.MediaException;
import com.wuan.wuan_news.wuan_news_server.mapper.MediaMapper;
import com.wuan.wuan_news.wuan_news_server.service.MediaService;
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

    @Autowired
    public MediaServiceImpl(MediaMapper mediaMapper) {
        this.mediaMapper = mediaMapper;
    }

    @Override
    public MediaDTO createMedia(MediaDTO mediaDTO) {
        Integer result = mediaMapper.insert(mediaDTO);
        if (result == 0) {
            throw new MediaException("创建媒体失败");
        } else {
            return mediaMapper.findByName(mediaDTO.getName());
        }
    }
}
