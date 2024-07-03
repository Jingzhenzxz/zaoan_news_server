package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import generator.domain.News;
import generator.service.NewsService;
import generator.mapper.NewsMapper;
import org.springframework.stereotype.Service;

/**
* @author Jingzhen
* @description 针对表【news】的数据库操作Service实现
* @createDate 2024-07-01 17:37:59
*/
@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News>
    implements NewsService{

}




