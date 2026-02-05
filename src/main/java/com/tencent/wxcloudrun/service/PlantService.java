package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.dto.req.PageQueryRequest;
import com.tencent.wxcloudrun.dto.req.PlantRequest;
import com.tencent.wxcloudrun.dto.resp.PageResponse;
import com.tencent.wxcloudrun.model.Plant;

/**
 * 植物管理服务接口
 * @author zszleon
 */
public interface PlantService {

    /**
     * 根据用户ID获取植物列表（分页）
     */
    PageResponse<Plant> getPlantsByUserId(PageQueryRequest request);

    /**
     * 根据ID获取植物详情（需要验证用户权限）
     */
    Plant getPlantById(Long id, Long userId);

    /**
     * 创建新植物
     */
    Plant createPlant(PlantRequest request);

    /**
     * 更新植物信息（需要验证用户权限）
     */
    Plant updatePlant(Long id, PlantRequest request);

    /**
     * 删除植物（需要验证用户权限）
     */
    boolean deletePlant(Long id, Long userId);
}