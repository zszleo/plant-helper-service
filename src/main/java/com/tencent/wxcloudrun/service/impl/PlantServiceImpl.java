package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.dao.PlantMapper;
import com.tencent.wxcloudrun.dto.req.PlantRequest;
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
    public List<Plant> getPlantsByUserId(String userId) {
        return plantMapper.findByUserId(userId);
    }

    @Override
    public Plant getPlantById(Integer id, String userId) {
        return plantMapper.findByIdAndUserId(id, userId);
    }

    @Override
    public Plant createPlant(PlantRequest request) {
        Plant plant = new Plant();
        // 设置基本属性
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
        return plant;
    }

    @Override
    public Plant updatePlant(Integer id, PlantRequest request) {
        // 先验证权限
        Plant existingPlant = plantMapper.findByIdAndUserId(id, request.getUserId());
        if (existingPlant == null) {
            return null;
        }
        
        // 更新属性
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
        
        int result = plantMapper.update(existingPlant);
        return result > 0 ? existingPlant : null;
    }

    @Override
    public boolean deletePlant(Integer id, String userId) {
        int result = plantMapper.delete(id, userId);
        return result > 0;
    }
}