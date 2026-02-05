package com.tencent.wxcloudrun.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 生长记录请求DTO
 * @author zszleon
 */
@Data
public class RecordRequest {
    /** 记录ID */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    /** 用户ID */
    private Long userId;
    
    /** 关联植物ID */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long plantId;
    
    /** 记录类型：watering/fertilizing/growth/photo */
    private String type;
    
    /** 记录时间 */
    private LocalDateTime recordTime;
    
    /** 备注信息 */
    private String notes;
    
    /** 图片URL（拍照记录时使用） */
    private String imageUrl;
}