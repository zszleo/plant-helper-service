package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.dto.req.LogRequestDTO;

public interface LogService {
    
    /**
     * 创建日志
     * @param request 日志请求DTO
     * @return 是否创建成功
     */
    boolean createLog(LogRequestDTO request);
}