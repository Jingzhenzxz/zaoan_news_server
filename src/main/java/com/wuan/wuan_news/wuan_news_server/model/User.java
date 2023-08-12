package com.wuan.wuan_news.wuan_news_server.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/07/30/ 13:28
 * @description
 */
@Data
@Schema(description = "Model representing a user")
public class User {

    @Schema(description = "Unique identifier for the user", example = "123")
    private Long id;

    @Schema(description = "Username of the user", example = "john_doe")
    private String username;

    @Schema(description = "Email address of the user", example = "john.doe@example.com")
    private String email;

    // 将密码字段标记为敏感数据，因此不在 Swagger 文档中显示它。
    @Schema(accessMode = Schema.AccessMode.WRITE_ONLY, description = "Password of the user (write-only)")
    private String password;

    @Schema(description = "Creation date of the user record", example = "2023-07-28T10:15:30")
    private LocalDateTime createdAt;

    @Schema(description = "Last update date of the user record", example = "2023-07-29T10:15:30")
    private LocalDateTime updatedAt;
}
