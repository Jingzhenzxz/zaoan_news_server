package com.wuan.wuan_news.wuan_news_server.controller;

import com.wuan.wuan_news.wuan_news_server.dto.MediaDTO;
import com.wuan.wuan_news.wuan_news_server.dto.MediaRequestDTO;
import com.wuan.wuan_news.wuan_news_server.dto.MediaResponseDTO;
import com.wuan.wuan_news.wuan_news_server.exception.UnauthorizedException;
import com.wuan.wuan_news.wuan_news_server.service.MediaService;
import io.swagger.annotations.*;
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

@Api(tags = "Media Endpoints", value = "Endpoints for managing media")
@RestController
@RequestMapping("/api/media")
public class MediaController {
    private final MediaService mediaService;

    @Autowired
    private MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @ApiOperation(value = "Create a new media")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Media created successfully"),
            @ApiResponse(code = 400, message = "Bad Request, invalid input data"),
            @ApiResponse(code = 401, message = "Unauthorized, user not authenticated")
    })
    @PostMapping
    public ResponseEntity<MediaResponseDTO> createMedia(
            @ApiParam(value = "Media creation request object", required = true)
            @Valid @RequestBody MediaRequestDTO mediaRequestDTO,
            Principal principal) {
        if (principal == null) {
            throw new UnauthorizedException("User is not authenticated");
        }

        MediaDTO createdMediaDTO = mediaService.createMedia(mediaRequestDTO.getName(), mediaRequestDTO.getRssLink());
        return ResponseEntity.status(HttpStatus.CREATED).body(new MediaResponseDTO(createdMediaDTO));
    }

    @ApiOperation(value = "Delete media by media name")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Media deleted successfully"),
            @ApiResponse(code = 400, message = "Bad Request, invalid media name"),
            @ApiResponse(code = 401, message = "Unauthorized, user not authenticated")
    })
    @DeleteMapping("/{mediaName}")
    public ResponseEntity<Void> deleteMedia(
            @ApiParam(value = "Media name to delete", required = true)
            @PathVariable String mediaName, Principal principal) {
        if (principal == null) {
            throw new UnauthorizedException("User is not authenticated");
        }

        MediaDTO deletedMediaDTO = mediaService.deleteMediaByMediaName(mediaName);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
