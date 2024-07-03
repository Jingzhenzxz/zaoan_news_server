package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.Topic;
import generator.service.TopicService;
import generator.mapper.TopicMapper;
import org.springframework.stereotype.Service;

/**
* @author Jingzhen
* @description 针对表【topic】的数据库操作Service实现
* @createDate 2024-06-30 17:27:01
*/
@Service
public class TopicServiceImpl extends ServiceImpl<TopicMapper, Topic>
    implements TopicService{

}




