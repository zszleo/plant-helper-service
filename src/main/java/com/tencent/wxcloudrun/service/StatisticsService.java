package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.dto.req.HeatmapRequest;
import com.tencent.wxcloudrun.dto.resp.HeatmapResponse;
import com.tencent.wxcloudrun.dto.resp.PlantStatusResponse;
import com.tencent.wxcloudrun.dto.resp.RecordTypesResponse;

/**
 * 统计服务接口
 * @author zszleon
 */
public interface StatisticsService {
    
    /**
     * 获取植物状态分布统计
     * @param userId 用户ID
     * @return 植物状态分布响应
     */
    PlantStatusResponse getPlantStatusStatistics(Long userId);
    
    /**
     * 获取记录类型分布统计
     * @param userId 用户ID
     * @return 记录类型分布响应
     */
    RecordTypesResponse getRecordTypesStatistics(Long userId);
    
    /**
     * 获取热力图数据
     * @param userId 用户ID
     * @param request 热力图请求参数
     * @return 热力图响应
     */
    HeatmapResponse getHeatmapStatistics(Long userId, HeatmapRequest request);
}