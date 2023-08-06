package com.wuan.wuan_news.wuan_news_server.controller;

import com.wuan.wuan_news.wuan_news_server.dto.MediaDTO;
import com.wuan.wuan_news.wuan_news_server.dto.MediaRequestDTO;
import com.wuan.wuan_news.wuan_news_server.dto.MediaResponseDTO;
import com.wuan.wuan_news.wuan_news_server.exception.UnauthorizedException;
import com.wuan.wuan_news.wuan_news_server.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

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
    public ResponseEntity<MediaResponseDTO> createMedia(@Valid @RequestBody MediaRequestDTO mediaRequestDTO, Principal principal) {
        if (principal == null) {
            throw new UnauthorizedException("User is not authenticated");
        }

        MediaDTO createdMediaDTO  = mediaService.createMedia(mediaRequestDTO.getName(), mediaRequestDTO.getRssLink());
        return ResponseEntity.status(HttpStatus.CREATED).body(new MediaResponseDTO("添加媒体成功", createdMediaDTO));
    }

    @DeleteMapping("/{mediaName}")
    public ResponseEntity<MediaResponseDTO> deleteMedia(@PathVariable String mediaName, Principal principal) {
        if (principal == null) {
            throw new UnauthorizedException("User is not authenticated");
        }

        MediaDTO deletedMediaDTO  = mediaService.deleteMediaByMediaName(mediaName);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MediaResponseDTO("删除媒体成功", deletedMediaDTO));
    }
}
