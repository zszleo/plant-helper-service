package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.auth.LoginUser;
import com.tencent.wxcloudrun.dto.req.HeatmapRequest;
import com.tencent.wxcloudrun.dto.resp.ApiResponse;
import com.tencent.wxcloudrun.dto.resp.HeatmapResponse;
import com.tencent.wxcloudrun.dto.resp.PlantStatusResponse;
import com.tencent.wxcloudrun.dto.resp.RecordTypesResponse;
import com.tencent.wxcloudrun.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 统计控制器
 * @author zszleon
 */
@Tag(name = "统计管理", description = "数据统计相关接口")
@Slf4j
@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Resource
    private StatisticsService statisticsService;

    /**
     * 获取植物状态分布统计
     */
    @Operation(summary = "获取植物状态分布", description = "获取各状态植物数量及占比（包含植物总数）")
    @PostMapping("/plant-status")
    public ApiResponse<PlantStatusResponse> getPlantStatusStatistics(@Parameter(hidden = true) LoginUser loginUser) {
        log.info("获取植物状态分布，用户ID: {}", loginUser.getUserId());
        return ApiResponse.ok(statisticsService.getPlantStatusStatistics(loginUser.getUserId()));
    }

    /**
     * 获取记录类型分布统计
     */
    @Operation(summary = "获取记录类型分布", description = "获取各类型记录数量及占比（包含记录总数，统计所有数据不限定时间）")
    @PostMapping("/record-types")
    public ApiResponse<RecordTypesResponse> getRecordTypesStatistics(@Parameter(hidden = true) LoginUser loginUser) {
        log.info("获取记录类型分布，用户ID: {}", loginUser.getUserId());
        return ApiResponse.ok(statisticsService.getRecordTypesStatistics(loginUser.getUserId()));
    }

    /**
     * 获取热力图数据
     */
    @Operation(summary = "获取热力图数据", description = "获取指定月份的热力图聚合数据")
    @PostMapping("/heatmap")
    public ApiResponse<HeatmapResponse> getHeatmapStatistics(@Valid @RequestBody HeatmapRequest request,
                                                             @Parameter(hidden = true) LoginUser loginUser) {
        log.info("获取热力图数据，用户ID: {}, 年份: {}, 月份: {}", loginUser.getUserId(), request.getYear(), request.getMonth());
        return ApiResponse.ok(statisticsService.getHeatmapStatistics(loginUser.getUserId(), request));
    }
}