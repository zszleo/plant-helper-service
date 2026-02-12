package com.tencent.wxcloudrun.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 提醒信息响应DTO
 * @author zszleon
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "提醒信息响应DTO")
public class ReminderResponse {
    
    @Schema(description = "提醒ID", example = "123456789", type = "string")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    
    @Schema(description = "关联植物ID", example = "123456789", type = "string")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long plantId;
    
    @Schema(description = "植物名称", example = "绿萝")
    private String plantName;
    
    @Schema(description = "提醒类型", allowableValues = {"watering", "fertilizing", "custom"}, example = "watering")
    private String type;
    
    @Schema(description = "自定义类型名称（当type为custom时）", example = "修剪")
    private String customType;
    
    @Schema(description = "提醒时间（HH:mm格式）", example = "09:00")
    private String time;
    
    @Schema(description = "频率（天数）", example = "7")
    private Integer frequency;
    
    @Schema(description = "频率类型", allowableValues = {"daily", "weekly", "monthly", "custom"}, example = "weekly")
    private String frequencyType;
    
    @Schema(description = "下次提醒时间", example = "2026-02-11 08:59:59")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date nextRemindTime;
    
    @Schema(description = "是否启用", example = "true")
    private Boolean isEnabled;
    
    @Schema(description = "创建时间", example = "2026-02-11 08:59:59")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
