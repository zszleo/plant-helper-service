package com.tencent.wxcloudrun.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 植物信息响应DTO
 * @author zszleon
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "植物信息响应DTO")
public class PlantResponse {
    
    @Schema(description = "植物ID", example = "123456789", type = "string")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    
    @Schema(description = "植物名称", example = "绿萝")
    private String name;
    
    @Schema(description = "植物类型", example = "观叶植物")
    private String type;
    
    @Schema(description = "描述信息", example = "喜阴，适合室内种植")
    private String description;
    
    @Schema(description = "图片URL", example = "https://example.com/plant.jpg")
    private String imageUrl;
    
    @Schema(description = "种植日期", example = "1770785605969")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date plantDate;
    
    @Schema(description = "状态", allowableValues = {"healthy", "growing", "need-care", "diseased"}, example = "healthy")
    private String status;
    
    @Schema(description = "浇水间隔（天）", example = "7")
    private Integer wateringInterval;
    
    @Schema(description = "施肥间隔（天）", example = "30")
    private Integer fertilizingInterval;
    
    @Schema(description = "上次浇水时间", example = "2026-02-11 08:59:59")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastWatering;
    
    @Schema(description = "上次施肥时间", example = "1770785605969")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date lastFertilizing;
    
    @Schema(description = "创建时间", example = "1770785605969")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date createTime;
}
