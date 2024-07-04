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
public class MediaUpdateRequest implements Serializable {
    @NotEmpty(message = "媒体编码不能为空")
    private Long id;
    private String name;
    private String rssLink;
    @NotEmpty(message = "用户编码不能为空")
    private Long userId;
    private static final long serialVersionUID = 1L;
}
