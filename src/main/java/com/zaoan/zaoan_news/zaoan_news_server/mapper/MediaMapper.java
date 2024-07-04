package com.zaoan.zaoan_news.zaoan_news_server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zaoan.zaoan_news.zaoan_news_server.model.entity.Media;
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
public interface MediaMapper extends BaseMapper<Media> {
}
