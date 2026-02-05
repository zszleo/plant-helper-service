package com.tencent.wxcloudrun.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 提醒信息请求DTO
 * @author zszleon
 */
@Data
public class ReminderRequest {
    /** 提醒ID */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    /** 用户ID */
    private Long userId;
    
    /** 关联植物ID */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long plantId;
    
    /** 提醒类型：watering/fertilizing/custom */
    private String type;
    
    /** 自定义类型名称（当type为custom时） */
    private String customType;
    
    /** 提醒时间（HH:mm格式） */
    private String time;
    
    /** 频率（天数） */
    private Integer frequency;
    
    /** 频率类型：daily/weekly/monthly/custom */
    private String frequencyType;
    
    /** 下次提醒时间 */
    private LocalDateTime nextRemindTime;
    
    /** 是否启用 */
    private Boolean isEnabled;
}