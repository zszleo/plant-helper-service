package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.dto.req.CommonRequest;
import com.tencent.wxcloudrun.dto.resp.ApiResponse;
import com.tencent.wxcloudrun.dto.resp.PageResponse;
import com.tencent.wxcloudrun.dto.req.PageQueryRequest;
import com.tencent.wxcloudrun.dto.req.PlantRequest;
import com.tencent.wxcloudrun.model.Plant;
import com.tencent.wxcloudrun.service.PlantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 植物管理控制器
 * @author zszleon
 */
@Slf4j
@RestController
@RequestMapping("/api/plants")
public class PlantController {

    @Resource
    private PlantService plantService;

    /**
     * 获取用户的所有植物（分页）
     */
    @PostMapping("/page")
    public ApiResponse<PageResponse<Plant>> getPlantsPage(@Valid @RequestBody PageQueryRequest request) {
        log.info("获取植物列表, 用户: {}, 页码: {}, 每页大小: {}", request.getUserId(), request.getPageNum(), request.getPageSize());
        PageResponse<Plant> pageData = plantService.getPlantsByUserId(request);
        return ApiResponse.pageOk(pageData);
    }

    /**
     * 根据ID获取植物详情
     */
    @PostMapping("/getPlantById")
    public ApiResponse<Plant> getPlantById(@RequestBody CommonRequest.Id id,
                                   @RequestHeader("X-User-ID") String userId) {
        log.info("获取植物详情, 用户: {}, 植物ID: {}", userId, id.getId());
        Plant plant = plantService.getPlantById(id.getId(), userId);
        if (plant != null) {
            return ApiResponse.ok(plant);
        } else {
            return ApiResponse.error("植物不存在或无权访问");
        }
    }

    /**
     * 创建新植物
     */
    @PostMapping("/createPlant")
    public ApiResponse<Plant> createPlant(@RequestBody PlantRequest request,
                                  @RequestHeader("X-User-ID") String userId) {
        // 设置用户ID
        request.setUserId(userId);
        log.info("创建植物, 用户: {}, 植物名称: {}", userId, request.getName());
        Plant plant = plantService.createPlant(request);
        return ApiResponse.ok(plant);
    }

    /**
     * 更新植物信息
     */
    @PostMapping("/update")
    public ApiResponse<Plant> updatePlant(@RequestBody CommonRequest.Id id,
                                  @RequestBody PlantRequest request,
                                  @RequestHeader("X-User-ID") String userId) {
        // 设置用户ID
        request.setUserId(userId);
        log.info("更新植物, 用户: {}, 植物ID: {}, 植物名称: {}", userId, id.getId(), request.getName());
        Plant plant = plantService.updatePlant(id.getId(), request);
        if (plant != null) {
            return ApiResponse.ok(plant);
        } else {
            return ApiResponse.error("植物不存在或无权修改");
        }
    }

    /**
     * 删除植物
     */
    @PostMapping("/delete")
    public ApiResponse<String> deletePlant(@RequestBody CommonRequest.Id id,
                                  @RequestHeader("X-User-ID") String userId) {
        log.info("删除植物, 用户: {}, 植物ID: {}", userId, id.getId());
        boolean success = plantService.deletePlant(id.getId(), userId);
        if (success) {
            return ApiResponse.ok("删除成功");
        } else {
            return ApiResponse.error("植物不存在或无权删除");
        }
    }
}