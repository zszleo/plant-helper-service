package com.tencent.wxcloudrun.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 生长记录响应DTO
 * @author zszleon
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "生长记录响应DTO")
public class RecordResponse {
    
    @Schema(description = "记录ID", example = "123456789", type = "string")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    
    @Schema(description = "关联植物ID", example = "123456789", type = "string")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long plantId;
    
    @Schema(description = "植物名称", example = "绿萝")
    private String plantName;
    
    @Schema(description = "记录类型", allowableValues = {"watering", "fertilizing", "growth", "photo"}, example = "watering")
    private String type;
    
    @Schema(description = "记录时间", example = "1770785605969")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date recordTime;
    
    @Schema(description = "备注信息", example = "浇水500ml")
    private String notes;
    
    @Schema(description = "图片URL", example = "https://example.com/record.jpg")
    private String imageUrl;
    
    @Schema(description = "创建时间", example = "1770785605969")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date createTime;
}
