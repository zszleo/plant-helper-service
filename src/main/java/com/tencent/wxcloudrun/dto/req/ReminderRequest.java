package com.tencent.wxcloudrun.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 提醒信息请求DTO
 * @author zszleon
 */
@Data
@Schema(description = "提醒信息请求DTO")
public class ReminderRequest {
    /** 提醒ID */
    @Schema(description = "提醒ID，更新时必填", example = "123456789")
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
    
    /** 提醒类型：watering/fertilizing/custom */
    @Schema(description = "提醒类型", allowableValues = {"watering", "fertilizing", "custom"}, example = "watering")
    private String type;
    
    /** 自定义类型名称（当type为custom时） */
    @Schema(description = "自定义类型名称（当type为custom时）", example = "修剪")
    private String customType;
    
    /** 提醒时间（HH:mm格式） */
    @Schema(description = "提醒时间（HH:mm格式）", example = "09:00")
    private String time;
    
    /** 频率（天数） */
    @Schema(description = "频率（天数）", example = "7")
    private Integer frequency;
    
    /** 频率类型：daily/weekly/monthly/custom */
    @Schema(description = "频率类型", allowableValues = {"daily", "weekly", "monthly", "custom"}, example = "weekly")
    private String frequencyType;
    
    /** 下次提醒时间 */
    @Schema(description = "下次提醒时间", example = "2024-01-01T09:00:00")
    private LocalDateTime nextRemindTime;
    
    /** 是否启用 */
    @Schema(description = "是否启用", example = "true")
    private Boolean isEnabled;
}