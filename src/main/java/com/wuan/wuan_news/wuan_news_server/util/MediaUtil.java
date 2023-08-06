package com.wuan.wuan_news.wuan_news_server.util;

import com.wuan.wuan_news.wuan_news_server.dto.MediaDTO;
import com.wuan.wuan_news.wuan_news_server.dto.UserDTO;
import com.wuan.wuan_news.wuan_news_server.model.Media;
import com.wuan.wuan_news.wuan_news_server.model.User;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/08/05/ 11:13
 * @description
 */
@Service
public class MediaUtil {
    public MediaDTO convertMediaModelToMediaDTO(Media media) {
        if (media == null) {
            return null;
        }
        MediaDTO mediaDTO = new MediaDTO();
        mediaDTO.setName(media.getName());
        mediaDTO.setRssLink(media.getRssLink());
        mediaDTO.setCreatedAt(media.getCreatedAt());
        mediaDTO.setUpdatedAt(media.getUpdatedAt());

        return mediaDTO;
    }

    public Media convertMediaDTOToMediaModel(MediaDTO mediaDTO) {
        if (mediaDTO == null) {
            return null;
        }

        Media media = new Media();
        media.setName(mediaDTO.getName());
        media.setRssLink(mediaDTO.getRssLink());
        media.setCreatedAt(mediaDTO.getCreatedAt());
        media.setUpdatedAt(mediaDTO.getUpdatedAt());

        return media;
    }
}
