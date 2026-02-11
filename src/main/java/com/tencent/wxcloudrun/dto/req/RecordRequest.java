package com.tencent.wxcloudrun.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.Date;

/**
 * 生长记录请求DTO
 * @author zszleon
 */
@Data
@Schema(description = "生长记录请求DTO")
public class RecordRequest {
    /** 记录ID */
    @Schema(description = "记录ID，更新时必填", example = "123456789")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    
    /** 用户ID */
    @Schema(description = "用户ID，系统自动设置", hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;
    
    /** 关联植物ID */
    @Schema(description = "关联植物ID", example = "123456789")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long plantId;
    
    /** 记录类型：watering/fertilizing/growth/photo */
    @Schema(description = "记录类型", allowableValues = {"watering", "fertilizing", "growth", "photo"}, example = "watering")
    private String type;
    
    /** 记录时间 */
    @Schema(description = "记录时间", example = "2024-01-01T10:00:00")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date recordTime;
    
    /** 备注信息 */
    @Schema(description = "记录备注", example = "今天浇水了")
    private String notes;
    
    /** 图片URL（拍照记录时使用） */
    @Schema(description = "图片URL（拍照记录时使用）", example = "https://example.com/image.jpg")
    private String imageUrl;
}