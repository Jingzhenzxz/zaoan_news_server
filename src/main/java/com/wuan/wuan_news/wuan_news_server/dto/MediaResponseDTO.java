package com.wuan.wuan_news.wuan_news_server.dto;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/07/30/ 13:51
 * @description
 */
public class MediaResponseDTO {
    private String message;
    private MediaDTO mediaDTO;

    public MediaResponseDTO(String message, MediaDTO mediaDTO) {
        this.message = message;
        this.mediaDTO = mediaDTO;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MediaDTO getMediaDTO() {
        return mediaDTO;
    }

    public void setMediaDTO(MediaDTO mediaDTO) {
        this.mediaDTO = mediaDTO;
    }
}
