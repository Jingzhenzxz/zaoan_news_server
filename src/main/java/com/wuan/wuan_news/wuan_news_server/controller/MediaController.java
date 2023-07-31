package com.wuan.wuan_news.wuan_news_server.controller;

import com.wuan.wuan_news.wuan_news_server.dto.MediaDTO;
import com.wuan.wuan_news.wuan_news_server.dto.MediaResponseDTO;
import com.wuan.wuan_news.wuan_news_server.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/07/30/ 12:59
 * @description
 */

@RestController
@RequestMapping("/api/media")
public class MediaController {
    private final MediaService mediaService;

    @Autowired
    private MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @PostMapping
    public ResponseEntity<?> createMedia(@Valid @RequestBody MediaDTO mediaDTO) {
        MediaDTO newMediaDTO  = mediaService.createMedia(mediaDTO);
        return new ResponseEntity<>(new MediaResponseDTO("添加媒体成功", newMediaDTO), HttpStatus.OK);
    }
}
