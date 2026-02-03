package com.tencent.wxcloudrun.dto.req;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 植物信息请求DTO
 * @author zszleon
 */
@Data
public class PlantRequest {
    /** 用户ID */
    private String userId;
    
    /** 植物名称 */
    private String name;
    
    /** 植物类型 */
    private String type;
    
    /** 描述信息 */
    private String description;
    
    /** 图片URL */
    private String imageUrl;
    
    /** 种植日期 */
    private LocalDateTime plantDate;
    
    /** 状态：healthy/growing/need-care/diseased */
    private String status;
    
    /** 浇水间隔（天） */
    private Integer wateringInterval;
    
    /** 施肥间隔（天） */
    private Integer fertilizingInterval;
    
    /** 上次浇水时间 */
    private LocalDateTime lastWatering;
    
    /** 上次施肥时间 */
    private LocalDateTime lastFertilizing;
}