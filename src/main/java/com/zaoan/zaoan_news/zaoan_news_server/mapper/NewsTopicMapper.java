package com.zaoan.zaoan_news.zaoan_news_server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zaoan.zaoan_news.zaoan_news_server.model.entity.NewsTopic;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/09/02/ 15:58
 * @description
 */
@Mapper
public interface NewsTopicMapper extends BaseMapper<NewsTopic> {
}
