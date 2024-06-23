package com.wuan.wuan_news.wuan_news_server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wuan.wuan_news.wuan_news_server.model.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/07/30/ 16:06
 * @description
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
