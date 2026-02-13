package com.tencent.wxcloudrun.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

@Data
@TableName("wx_mini_program_log")
public class Log {
    
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    
    private String code;
    
    private String level;
    
    private String context;
    
    private String apiName;
    
    private String operation;
    
    private String params;
    
    private String path;
    
    private String deviceInfo;
    
    private String message;
    
    private String processingResult;
    
    private String source;
    
    private Date logTimestamp;
    
    private String type;
    
    private String userMessage;
    
    private Date createTime;
    
    private Date updateTime;
}