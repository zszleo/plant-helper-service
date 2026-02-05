package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.auth.LoginUser;
import com.tencent.wxcloudrun.dto.req.CommonRequest;
import com.tencent.wxcloudrun.dto.resp.ApiResponse;
import com.tencent.wxcloudrun.dto.resp.PageResponse;
import com.tencent.wxcloudrun.dto.req.PageQueryRequest;
import com.tencent.wxcloudrun.dto.req.PlantRequest;
import com.tencent.wxcloudrun.model.Plant;
import com.tencent.wxcloudrun.service.PlantService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * 植物管理控制器
 * @author zszleon
 */
@Tag(name = "植物管理", description = "植物的增删改查、分页查询等接口")
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
    public ApiResponse<PageResponse<Plant>> getPlantsPage(@Valid @RequestBody PageQueryRequest request,
                                                          @Parameter(hidden = true) LoginUser loginUser) {
        log.info("获取植物列表, 用户: {}, 页码: {}, 每页大小: {}", loginUser.getOpenid(), request.getPageNum(), request.getPageSize());
        request.setUserId(loginUser.getOpenid());
        PageResponse<Plant> pageData = plantService.getPlantsByUserId(request);
        return ApiResponse.pageOk(pageData);
    }

    /**
     * 根据ID获取植物详情
     */
    @PostMapping("/getPlantById")
    public ApiResponse<Plant> getPlantById(@RequestBody CommonRequest.Id id,
                                           @Parameter(hidden = true) LoginUser loginUser) {
        log.info("获取植物详情, 用户: {}, 植物ID: {}", loginUser.getOpenid(), id.getId());
        Plant plant = plantService.getPlantById(id.getId(), loginUser.getOpenid());
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
                                          @Parameter(hidden = true) LoginUser loginUser) {
        // 设置用户ID
        request.setUserId(loginUser.getOpenid());
        log.info("创建植物, 用户: {}, 植物名称: {}", loginUser.getOpenid(), request.getName());
        Plant plant = plantService.createPlant(request);
        return ApiResponse.ok(plant);
    }

    /**
     * 更新植物信息
     */
    @PostMapping("/update")
    public ApiResponse<Plant> updatePlant(@RequestBody PlantRequest request,
                                          @Parameter(hidden = true) LoginUser loginUser) {
        // 设置用户ID
        request.setUserId(loginUser.getOpenid());
        log.info("更新植物, 用户: {}, 植物ID: {}, 植物名称: {}", loginUser.getOpenid(), request.getId(), request.getName());
        Plant plant = plantService.updatePlant(request.getId(), request);
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
                                           @Parameter(hidden = true) LoginUser loginUser) {
        log.info("删除植物, 用户: {}, 植物ID: {}", loginUser.getOpenid(), id.getId());
        boolean success = plantService.deletePlant(id.getId(), loginUser.getOpenid());
        if (success) {
            return ApiResponse.ok("删除成功");
        } else {
            return ApiResponse.error("植物不存在或无权删除");
        }
    }
}