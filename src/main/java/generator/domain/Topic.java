package generator.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 
 * @TableName topic
 */
@TableName(value ="topic")
@Data
public class Topic implements Serializable {
    /**
     * 话题编码
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 话题名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 创建者编码
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 最新的一条新闻的编码
     */
    @TableField(value = "latest_news_1_id")
    private Long latestNews1Id;

    /**
     * 最新的第二条新闻的编码
     */
    @TableField(value = "latest_news_2_id")
    private Long latestNews2Id;

    /**
     * 最新的第三条新闻的编码
     */
    @TableField(value = "latest_news_3_id")
    private Long latestNews3Id;

    /**
     * 今日新增的新闻数量
     */
    @TableField(value = "new_content_today_count")
    private Integer newContentTodayCount;

    /**
     * 创建时间
     */
    @TableField(value = "created_at")
    private LocalDateTime createdAt;

    /**
     * 修改时间
     */
    @TableField(value = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * 是否被删除
     */
    @TableField(value = "is_delete")
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}