package com.wuan.wuan_news.wuan_news_server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wuan.wuan_news.wuan_news_server.model.entity.Topic;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/09/02/ 15:42
 * @description
 */
@Mapper
public interface TopicMapper extends BaseMapper<Topic> {
}
