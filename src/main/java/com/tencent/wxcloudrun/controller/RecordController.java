package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.auth.LoginUser;
import com.tencent.wxcloudrun.dto.req.CommonRequest;
import com.tencent.wxcloudrun.dto.resp.ApiResponse;
import com.tencent.wxcloudrun.dto.resp.PageResponse;
import com.tencent.wxcloudrun.dto.req.RecordPageQueryRequest;
import com.tencent.wxcloudrun.dto.req.RecordRequest;
import com.tencent.wxcloudrun.model.Record;
import com.tencent.wxcloudrun.service.RecordService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * 生长记录管理控制器
 * @author zszleon
 */
@Tag(name = "生长记录管理", description = "植物生长记录（浇水、施肥、生长、拍照）的增删改查、分页查询等接口")
@Slf4j
@RestController
@RequestMapping("/api/records")
public class RecordController {

    @Resource
    private RecordService recordService;

    /**
     * 获取用户的所有记录（分页）
     */
    @PostMapping("/page")
    public ApiResponse<PageResponse<Record>> getRecordsPage(@Valid @RequestBody RecordPageQueryRequest request,
                                                            @Parameter(hidden = true) LoginUser loginUser) {
        // 设置用户ID
        request.setUserId(loginUser.getUserId());
        log.info("获取记录列表, 用户: {}, 页码: {}, 每页大小: {}", loginUser.getUserId(), request.getPageNum(), request.getPageSize());
        PageResponse<Record> pageData = recordService.getRecordsByPage(request);
        return ApiResponse.pageOk(pageData);
    }

    /**
     * 根据ID获取记录详情
     */
    @PostMapping("/getRecordById")
    public ApiResponse<Record> getRecordById(@RequestBody CommonRequest.Id id,
                                             @Parameter(hidden = true) LoginUser loginUser) {
        log.info("获取记录详情, 用户: {}, 记录ID: {}", loginUser.getUserId(), id.getId());

        Record record = recordService.getRecordById(id.getId(), loginUser.getUserId());
        if (record != null) {
            return ApiResponse.ok(record);
        } else {
            return ApiResponse.error("记录不存在或无权访问");
        }
    }

    /**
     * 创建新记录
     */
    @PostMapping("/createRecord")
    public ApiResponse<Record> createRecord(@RequestBody RecordRequest request,
                                            @Parameter(hidden = true) LoginUser loginUser) {
        log.info("创建记录, 用户: {}, 植物ID: {}, 记录类型: {}", loginUser.getUserId(), request.getPlantId(), request.getType());

        // 设置用户ID
        request.setUserId(loginUser.getUserId());
        Record record = recordService.createRecord(request);
        return ApiResponse.ok(record);
    }

    /**
     * 更新记录信息
     */
    @PostMapping("/update")
    public ApiResponse<Record> updateRecord(@RequestBody RecordRequest request,
                                            @Parameter(hidden = true) LoginUser loginUser) {
        log.info("更新记录, 用户: {}, 记录ID: {}, 记录类型: {}", loginUser.getUserId(), request.getId(), request.getType());
        // 设置用户ID
        request.setUserId(loginUser.getUserId());
        Record record = recordService.updateRecord(request.getId(), request);
        if (record != null) {
            return ApiResponse.ok(record);
        } else {
            return ApiResponse.error("记录不存在或无权修改");
        }
    }

    /**
     * 删除记录
     */
    @PostMapping("/delete")
    public ApiResponse<String> deleteRecord(@RequestBody CommonRequest.Id id,
                                            @Parameter(hidden = true) LoginUser loginUser) {
        log.info("删除记录, 用户: {}, 记录ID: {}", loginUser.getUserId(), id.getId());
        boolean success = recordService.deleteRecord(id.getId(), loginUser.getUserId());
        if (success) {
            return ApiResponse.ok("删除成功");
        } else {
            return ApiResponse.error("记录不存在或无权删除");
        }
    }
}