package com.zaoan.zaoan_news.zaoan_news_server.model.dto.media;

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
public class MediaAddRequest implements Serializable {
    @NotEmpty(message = "媒体名称不能为空")
    private String name;
    @NotEmpty(message = "RSS 地址不能为空")
    private String rssLink;
    private static final long serialVersionUID = 1L;
}
