package com.tencent.wxcloudrun.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.wxcloudrun.dao.PlantMapper;
import com.tencent.wxcloudrun.dto.req.PageQueryRequest;
import com.tencent.wxcloudrun.dto.req.PlantRequest;
import com.tencent.wxcloudrun.dto.resp.PageResponse;
import com.tencent.wxcloudrun.dto.resp.PlantResponse;
import com.tencent.wxcloudrun.model.Plant;
import com.tencent.wxcloudrun.service.PlantService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 植物管理服务实现类
 * @author zszleon
 */
@Service
public class PlantServiceImpl implements PlantService {

    @Resource
    private PlantMapper plantMapper;

    @Override
    public PageResponse<PlantResponse> getPlantsByUserId(PageQueryRequest request) {
        Page<Plant> page = new Page<>(request.getPageNum().longValue(), request.getPageSize().longValue());
        QueryWrapper<Plant> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", request.getUserId())
               .orderByDesc("create_time");
        plantMapper.selectPage(page, wrapper);
        List<PlantResponse> plantResponses = BeanUtil.copyToList(page.getRecords(), PlantResponse.class);
        return PageResponse.<PlantResponse>builder()
                .list(plantResponses)
                .total(page.getTotal())
                .pageNum(request.getPageNum())
                .pageSize(request.getPageSize())
                .totalPages((int) page.getPages())
                .build();
    }

    @Override
    public PlantResponse getPlantById(Long id, Long userId) {
        Plant plant = plantMapper.findByIdAndUserId(id, userId);
        if (plant == null) {
            return null;
        }
        return BeanUtil.copyProperties(plant, PlantResponse.class);
    }

    @Override
    public PlantResponse createPlant(PlantRequest request) {
        Plant plant = new Plant();
        plant.setUserId(request.getUserId());
        plant.setName(request.getName());
        plant.setType(request.getType());
        plant.setDescription(request.getDescription());
        plant.setImageUrl(request.getImageUrl());
        plant.setPlantDate(request.getPlantDate());
        plant.setStatus(request.getStatus() != null ? request.getStatus() : "healthy");
        plant.setWateringInterval(request.getWateringInterval() != null ? request.getWateringInterval() : 7);
        plant.setFertilizingInterval(request.getFertilizingInterval() != null ? request.getFertilizingInterval() : 30);
        plant.setLastWatering(request.getLastWatering());
        plant.setLastFertilizing(request.getLastFertilizing());

        plantMapper.insert(plant);

        return BeanUtil.copyProperties(plant, PlantResponse.class);
    }

    @Override
    public PlantResponse updatePlant(Long id, PlantRequest request) {
        Plant existingPlant = plantMapper.findByIdAndUserId(id, request.getUserId());
        if (existingPlant == null) {
            return null;
        }

        existingPlant.setName(request.getName());
        existingPlant.setType(request.getType());
        existingPlant.setDescription(request.getDescription());
        existingPlant.setImageUrl(request.getImageUrl());
        existingPlant.setPlantDate(request.getPlantDate());
        existingPlant.setStatus(request.getStatus());
        existingPlant.setWateringInterval(request.getWateringInterval());
        existingPlant.setFertilizingInterval(request.getFertilizingInterval());
        existingPlant.setLastWatering(request.getLastWatering());
        existingPlant.setLastFertilizing(request.getLastFertilizing());

        int result = plantMapper.updateById(existingPlant);
        return result > 0 ? BeanUtil.copyProperties(existingPlant, PlantResponse.class) : null;
    }

    @Override
    public boolean deletePlant(Long id, Long userId) {
        QueryWrapper<Plant> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id).eq("user_id", userId);
        int result = plantMapper.delete(wrapper);
        return result > 0;
    }
}