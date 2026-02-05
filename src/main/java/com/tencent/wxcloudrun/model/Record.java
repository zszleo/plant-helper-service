package com.tencent.wxcloudrun.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 生长记录实体类
 * @author zszleon
 */
@Data
@TableName("records")
public class Record {
    /** 主键ID */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    
    /** 用户ID */
    private Long userId;
    
    /** 关联植物ID */
    private Long plantId;
    
    /** 记录类型：watering/fertilizing/growth/photo */
    private String type;
    
    /** 记录时间 */
    private LocalDateTime recordTime;
    
    /** 备注信息 */
    private String notes;
    
    /** 图片URL（拍照记录时使用） */
    private String imageUrl;
    
    /** 创建时间 */
    private LocalDateTime createTime;
}