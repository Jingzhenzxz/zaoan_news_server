package com.wuan.wuan_news.wuan_news_server.controller;

import com.wuan.wuan_news.wuan_news_server.dto.*;
import com.wuan.wuan_news.wuan_news_server.exception.UnauthorizedException;
import com.wuan.wuan_news.wuan_news_server.service.TopicService;
import com.wuan.wuan_news.wuan_news_server.service.UserService;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/09/02/ 9:54
 * @description
 */
@Api(tags = "Topic Endpoints", value = "Endpoints for managing topic")
@RestController
@RequestMapping("/api/topic")
public class TopicController {
    private final TopicService topicService;
    private final UserService userService;

    public TopicController(TopicService topicService, UserService userService) {
        this.topicService = topicService;
        this.userService = userService;
    }

    // 创建话题
    @ApiOperation(value = "Create a new topic")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Topic created successfully"),
            @ApiResponse(code = 400, message = "Bad Request, invalid input data"),
            @ApiResponse(code = 401, message = "Unauthorized, user not authenticated")
    })
    @PostMapping
    public ResponseEntity<TopicResponseDTO> createTopic(
            @ApiParam(value = "Topic creation request object", required = true)
            @Valid @RequestBody TopicRequestDTO topicRequestDTO,
            Principal principal) {
        if (principal == null) {
            throw new UnauthorizedException("User is not authenticated");
        }

        TopicDTO createdTopicDTO = topicService.createTopicAndAssociateNews(topicRequestDTO.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(new TopicResponseDTO(createdTopicDTO));
    }

    // 获取所有话题卡片
    @ApiOperation(value = "Get all topic cards")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved topics list"),
            @ApiResponse(code = 401, message = "Unauthorized, user not authenticated")
    })
    @GetMapping("/cards")
    public ResponseEntity<TopicCardResponseDTO> getAllTopicCards() {
        List<TopicCardDTO> topicCardDTOList = topicService.getAllTopicCards();

        if (topicCardDTOList == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(new TopicCardResponseDTO(topicCardDTOList));
    }

    // 根据话题名称获取单个话题卡片
    @ApiOperation(value = "Get a single topic card by topic name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Topic card fetched successfully"),
            @ApiResponse(code = 400, message = "Bad Request, invalid input data"),
            @ApiResponse(code = 401, message = "Unauthorized, user not authenticated"),
            @ApiResponse(code = 404, message = "Not Found, topic not found")
    })
    @GetMapping("/cards/{topicName}")
    public ResponseEntity<TopicCardDTO> getSingleTopicCard(
            @ApiParam(value = "Name of the topic to fetch the card for", required = true)
            @PathVariable String topicName) {
        TopicCardDTO topicCard = topicService.getTopicCardByTopicName(topicName);

        if (topicCard == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(topicCard);
    }

    @ApiOperation(value = "Get details of a single topic by name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Topic fetched successfully"),
            @ApiResponse(code = 400, message = "Bad Request, invalid input data"),
            @ApiResponse(code = 401, message = "Unauthorized, user not authenticated"),
            @ApiResponse(code = 404, message = "Not Found, topic not found")
    })
    @GetMapping("/{topicName}")
    public ResponseEntity<TopicDetailDTO> getTopicDetail(
            @ApiParam(value = "Name of the topic to fetch the details for", required = true)
            @PathVariable String topicName) {

        TopicDetailDTO topicDetail = topicService.getTopicDetailByName(topicName);

        if (topicDetail == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(topicDetail);
    }
}
