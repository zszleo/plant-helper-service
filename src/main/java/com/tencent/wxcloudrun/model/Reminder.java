package com.tencent.wxcloudrun.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 提醒信息实体类
 * @author zszleon
 */
@Data
@TableName("reminders")
public class Reminder {
    /** 主键ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    
    /** 用户ID */
    private Long userId;
    
    /** 关联植物ID */
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
    
    /** 创建时间 */
    private LocalDateTime createTime;
    
    /** 更新时间 */
    private LocalDateTime updateTime;
}