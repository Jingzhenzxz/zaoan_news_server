package com.wuan.wuan_news.wuan_news_server.controller;

import com.wuan.wuan_news.wuan_news_server.dto.FollowRequestDTO;
import com.wuan.wuan_news.wuan_news_server.dto.TopicCardDTO;
import com.wuan.wuan_news.wuan_news_server.model.Topic;
import com.wuan.wuan_news.wuan_news_server.service.TopicService;
import com.wuan.wuan_news.wuan_news_server.service.UserService;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/10/23/ 13:52
 * @description
 */
@Api(tags = "User Endpoints", value = "Endpoints for managing user")
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final TopicService topicService;
    private final UserService userService;

    public UserController(TopicService topicService, UserService userService) {
        this.topicService = topicService;
        this.userService = userService;
    }

    @ApiOperation(value = "Get all topics followed by the user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Topics retrieved successfully"),
            @ApiResponse(code = 401, message = "Unauthorized, user not authenticated"),
            @ApiResponse(code = 404, message = "Not Found, user not found")
    })
    @GetMapping("/followedTopics")
    public ResponseEntity<?> getFollowedTopics(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        String email = principal.getName();
        Long userId = userService.getUserIdByEmail(email);
        List<String> topicNames = topicService.getFollowedTopicNamesByUserId(userId);

        if (topicNames == null || topicNames.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No topics found for the user");
        }

        return ResponseEntity.ok(topicNames);
    }

    @ApiOperation(value = "Follow a specific topic by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Topic followed successfully"),
            @ApiResponse(code = 400, message = "Bad Request, invalid input data"),
            @ApiResponse(code = 401, message = "Unauthorized, user not authenticated"),
            @ApiResponse(code = 404, message = "Not Found, topic not found")
    })
    @PostMapping("/followTopic")
    public ResponseEntity<?> followTopic(
            @ApiParam(value = "Data needed to follow a topic", required = true)
            @RequestBody FollowRequestDTO request,
            Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        String email = principal.getName();
        Long userId = userService.getUserIdByEmail(email);
        topicService.followTopic(userId, request.getTopicName());
        return ResponseEntity.ok("Followed successfully");
    }

    @ApiOperation(value = "Unfollow a specific topic by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Topic unfollowed successfully"),
            @ApiResponse(code = 400, message = "Bad Request, invalid input data"),
            @ApiResponse(code = 401, message = "Unauthorized, user not authenticated"),
            @ApiResponse(code = 404, message = "Not Found, topic not found")
    })
    @PostMapping("/unfollowTopic")
    public ResponseEntity<?> unfollowTopic(
            @ApiParam(value = "Data needed to unfollow a topic", required = true)
            @RequestBody FollowRequestDTO request,
            Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        String email = principal.getName();
        Long userId = userService.getUserIdByEmail(email);
        topicService.unfollowTopic(userId, request.getTopicName());
        return ResponseEntity.ok("Unfollowed successfully");
    }
}
