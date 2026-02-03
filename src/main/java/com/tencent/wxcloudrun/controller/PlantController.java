package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.dto.resp.ApiResponse;
import com.tencent.wxcloudrun.dto.req.PlantRequest;
import com.tencent.wxcloudrun.model.Plant;
import com.tencent.wxcloudrun.service.PlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 植物管理控制器
 * @author zszleon
 */
@RestController
@RequestMapping("/api/plants")
public class PlantController {

    private final PlantService plantService;

    @Autowired
    public PlantController(PlantService plantService) {
        this.plantService = plantService;
    }

    /**
     * 获取用户的所有植物
     */
    @GetMapping
    public ApiResponse<List<Plant>> getPlants(@RequestHeader("X-User-ID") String userId) {
        List<Plant> plants = plantService.getPlantsByUserId(userId);
        return ApiResponse.ok(plants);
    }

    /**
     * 根据ID获取植物详情
     */
    @GetMapping("/{id}")
    public ApiResponse<Plant> getPlantById(@PathVariable Integer id,
                                   @RequestHeader("X-User-ID") String userId) {
        Plant plant = plantService.getPlantById(id, userId);
        if (plant != null) {
            return ApiResponse.ok(plant);
        } else {
            return ApiResponse.error("植物不存在或无权访问");
        }
    }

    /**
     * 创建新植物
     */
    @PostMapping
    public ApiResponse<Plant> createPlant(@RequestBody PlantRequest request,
                                  @RequestHeader("X-User-ID") String userId) {
        // 设置用户ID
        request.setUserId(userId);
        Plant plant = plantService.createPlant(request);
        return ApiResponse.ok(plant);
    }

    /**
     * 更新植物信息
     */
    @PutMapping("/{id}")
    public ApiResponse<Plant> updatePlant(@PathVariable Integer id,
                                  @RequestBody PlantRequest request,
                                  @RequestHeader("X-User-ID") String userId) {
        // 设置用户ID
        request.setUserId(userId);
        Plant plant = plantService.updatePlant(id, request);
        if (plant != null) {
            return ApiResponse.ok(plant);
        } else {
            return ApiResponse.error("植物不存在或无权修改");
        }
    }

    /**
     * 删除植物
     */
    @DeleteMapping("/{id}")
    public ApiResponse<String> deletePlant(@PathVariable Integer id,
                                  @RequestHeader("X-User-ID") String userId) {
        boolean success = plantService.deletePlant(id, userId);
        if (success) {
            return ApiResponse.ok("删除成功");
        } else {
            return ApiResponse.error("植物不存在或无权删除");
        }
    }
}