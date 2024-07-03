package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.NewsTopic;
import generator.service.NewsTopicService;
import generator.mapper.NewsTopicMapper;
import org.springframework.stereotype.Service;

/**
* @author Jingzhen
* @description 针对表【news_topic】的数据库操作Service实现
* @createDate 2024-07-01 12:02:46
*/
@Service
public class NewsTopicServiceImpl extends ServiceImpl<NewsTopicMapper, NewsTopic>
    implements NewsTopicService{

}




