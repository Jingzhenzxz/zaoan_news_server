package com.wuan.wuan_news.wuan_news_server.service.impl;

import com.sun.media.jfxmedia.MediaException;
import com.wuan.wuan_news.wuan_news_server.dto.MediaDTO;
import com.wuan.wuan_news.wuan_news_server.exception.InvalidRSSFormatException;
import com.wuan.wuan_news.wuan_news_server.exception.MediaCreationException;
import com.wuan.wuan_news.wuan_news_server.exception.MediaDeleteException;
import com.wuan.wuan_news.wuan_news_server.exception.MediaNameAlreadyExists;
import com.wuan.wuan_news.wuan_news_server.mapper.MediaMapper;
import com.wuan.wuan_news.wuan_news_server.model.Media;
import com.wuan.wuan_news.wuan_news_server.service.MediaService;
import com.wuan.wuan_news.wuan_news_server.task.NewsFetchTask;
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
    private final NewsFetchTask newsFetchTask;
    @Autowired
    public MediaServiceImpl(MediaMapper mediaMapper, RssUtil rssUtil, MediaUtil mediaUtil, NewsFetchTask newsFetchTask) {
        this.mediaMapper = mediaMapper;
        this.rssUtil = rssUtil;
        this.mediaUtil = mediaUtil;
        this.newsFetchTask = newsFetchTask;
    }

    @Override
    public MediaDTO createMedia(String mediaName, String rssLink) {
        Media oldMedia = mediaMapper.findByName(mediaName);
        if (oldMedia != null) {
            throw new MediaNameAlreadyExists("media name already exists");
        }

        if (!rssUtil.isValidRssUrl(rssLink)) {
            throw new InvalidRSSFormatException("RSS 链接的格式有误");
        }

        MediaDTO mediaDTO = new MediaDTO();
        mediaDTO.setName(mediaName);
        mediaDTO.setRssLink(rssLink);
        Integer result = mediaMapper.insert(mediaUtil.convertMediaDTOToMediaModel(mediaDTO));
        if (result == 0) {
            throw new MediaCreationException("创建媒体失败");
        } else {
            newsFetchTask.fetchNewsFromRss();
            return mediaUtil.convertMediaModelToMediaDTO(mediaMapper.findByName(mediaDTO.getName()));
        }
    }

    @Override
    public MediaDTO deleteMediaByMediaName(String mediaName) {
        Media media = mediaMapper.findByName(mediaName);

        int result = mediaMapper.deleteByName(mediaName);
        if (result == 0) {
            throw new MediaDeleteException("删除媒体失败");
        }

        return mediaUtil.convertMediaModelToMediaDTO(media);
    }
}
