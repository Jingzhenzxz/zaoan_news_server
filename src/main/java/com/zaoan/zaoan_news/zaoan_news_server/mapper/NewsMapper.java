package com.zaoan.zaoan_news.zaoan_news_server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zaoan.zaoan_news.zaoan_news_server.model.entity.News;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/07/30/ 16:00
 * @description
 */
@Mapper
public interface NewsMapper extends BaseMapper<News> {
}
