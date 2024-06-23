package com.wuan.wuan_news.wuan_news_server.model.dto.media;

import com.wuan.wuan_news.wuan_news_server.common.PageRequest;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/07/30/ 15:24
 * @description
 */
@Data
public class MediaQueryRequest extends PageRequest implements Serializable {
    private Long id;
    private String name;
    private String rssLink;
    private Long userId;
    private static final long serialVersionUID = 1L;
}
