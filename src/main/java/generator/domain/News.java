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
 * @TableName news
 */
@TableName(value ="news")
@Data
public class News implements Serializable {
    /**
     * 新闻编码
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 新闻标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 新闻简介
     */
    @TableField(value = "description")
    private String description;

    /**
     * 预览图
     */
    @TableField(value = "preview_image")
    private String previewImage;

    /**
     * 发表日期
     */
    @TableField(value = "pub_date")
    private LocalDateTime pubDate;

    /**
     * 新闻地址
     */
    @TableField(value = "link")
    private String link;

    /**
     * 作者
     */
    @TableField(value = "author")
    private String author;

    /**
     * 媒体名称
     */
    @TableField(value = "media_name")
    private String mediaName;

    /**
     * 所属媒体编码
     */
    @TableField(value = "media_id")
    private Long mediaId;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}