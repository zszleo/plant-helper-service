package com.tencent.wxcloudrun.service.impl;

import cn.hutool.json.JSONUtil;
import com.tencent.wxcloudrun.dao.LogMapper;
import com.tencent.wxcloudrun.dto.req.LogRequestDTO;
import com.tencent.wxcloudrun.model.Log;
import com.tencent.wxcloudrun.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Service
public class LogServiceImpl implements LogService {

    @Resource
    private LogMapper logMapper;

    @Override
    public boolean createLog(LogRequestDTO request) {
        try {
            Log logEntity = new Log();
            
            // 手动设置字段
            logEntity.setCode(request.getCode());
            logEntity.setLevel(request.getLevel());
            logEntity.setContext(request.getContext());
            logEntity.setApiName(request.getApiName());
            logEntity.setOperation(request.getOperation());
            logEntity.setParams(request.getParams());
            logEntity.setPath(request.getPath());
            logEntity.setMessage(request.getMessage());
            logEntity.setSource(request.getSource());
            logEntity.setLogTimestamp(request.getTimestamp());
            logEntity.setType(request.getType());
            logEntity.setUserMessage(request.getUserMessage());
            
            // 处理设备信息JSON
            if (request.getDeviceInfo() != null) {
                logEntity.setDeviceInfo(JSONUtil.toJsonStr(request.getDeviceInfo()));
            }
            
            // 处理处理结果JSON
            if (request.getProcessingResult() != null) {
                logEntity.setProcessingResult(JSONUtil.toJsonStr(request.getProcessingResult()));
            }
            
            // 设置创建时间和更新时间
            Date now = new Date();
            logEntity.setCreateTime(now);
            logEntity.setUpdateTime(now);
            
            // 如果timestamp为空，使用当前时间
            if (logEntity.getLogTimestamp() == null) {
                logEntity.setLogTimestamp(now);
            }
            
            int result = logMapper.insert(logEntity);
            return result > 0;
            
        } catch (Exception e) {
            log.error("创建日志失败", e);
            return false;
        }
    }
}