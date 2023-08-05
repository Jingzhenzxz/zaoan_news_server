package com.wuan.wuan_news.wuan_news_server.mapper;

import com.wuan.wuan_news.wuan_news_server.dto.MediaDTO;
import com.wuan.wuan_news.wuan_news_server.model.Media;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/07/30/ 15:26
 * @description
 */
@Mapper
public interface MediaMapper {
    Integer insert(MediaDTO mediaDTO);

    Media findByName(String name);
    List<Media> getAllMedias();
}
