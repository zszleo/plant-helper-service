package com.tencent.wxcloudrun.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 植物信息请求DTO
 * @author zszleon
 */
@Data
@Schema(description = "植物信息请求DTO")
public class PlantRequest {
    /** 植物ID */
    @Schema(description = "植物ID，更新时必填", example = "123456789")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /** 用户ID */
    @Schema(description = "用户ID，系统自动设置", hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;
    
    /** 植物名称 */
    @Schema(description = "植物名称", example = "多肉植物")
    private String name;
    
    /** 植物类型 */
    @Schema(description = "植物类型", example = "多肉植物")
    private String type;
    
    /** 描述信息 */
    @Schema(description = "植物描述", example = "这是一盆可爱的多肉植物")
    private String description;
    
    /** 图片URL */
    @Schema(description = "植物图片URL", example = "https://example.com/image.jpg")
    private String imageUrl;
    
    /** 种植日期 */
    @Schema(description = "种植日期", example = "2024-01-01T10:00:00")
    private Date plantDate;
    
    /** 状态：healthy/growing/need-care/diseased */
    @Schema(description = "植物状态", allowableValues = {"healthy", "growing", "need-care", "diseased"}, example = "healthy")
    private String status;
    
    /** 浇水间隔（天） */
    @Schema(description = "浇水间隔天数", example = "7")
    private Integer wateringInterval;
    
    /** 施肥间隔（天） */
    @Schema(description = "施肥间隔天数", example = "30")
    private Integer fertilizingInterval;
    
    /** 上次浇水时间 */
    @Schema(description = "上次浇水时间", example = "2024-01-01T10:00:00")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date lastWatering;
    
    /** 上次施肥时间 */
    @Schema(description = "上次施肥时间", example = "2024-01-01T10:00:00")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date lastFertilizing;
}