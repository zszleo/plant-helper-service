package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.dto.resp.ApiResponse;
import com.tencent.wxcloudrun.dto.req.RecordRequest;
import com.tencent.wxcloudrun.model.Record;
import com.tencent.wxcloudrun.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 生长记录管理控制器
 * @author zszleon
 */
@RestController
@RequestMapping("/api/records")
public class RecordController {

    @Resource
    private RecordService recordService;

    /**
     * 获取用户的所有记录
     */
    @GetMapping
    public ApiResponse<List<Record>> getRecords(@RequestHeader("X-User-ID") String userId) {
        try {
            List<Record> records = recordService.getRecordsByUserId(userId);
            return ApiResponse.ok(records);
        } catch (Exception e) {
            return ApiResponse.error("获取记录列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据植物ID获取记录
     */
    @GetMapping("/plant/{plantId}")
    public ApiResponse<List<Record>> getRecordsByPlantId(@PathVariable Integer plantId,
                                          @RequestHeader("X-User-ID") String userId) {
        try {
            List<Record> records = recordService.getRecordsByPlantId(plantId, userId);
            return ApiResponse.ok(records);
        } catch (Exception e) {
            return ApiResponse.error("获取植物记录失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID获取记录详情
     */
    @GetMapping("/{id}")
    public ApiResponse<Record> getRecordById(@PathVariable Integer id,
                                    @RequestHeader("X-User-ID") String userId) {
        try {
            Record record = recordService.getRecordById(id, userId);
            if (record != null) {
                return ApiResponse.ok(record);
            } else {
                return ApiResponse.error("记录不存在或无权访问");
            }
        } catch (Exception e) {
            return ApiResponse.error("获取记录详情失败: " + e.getMessage());
        }
    }

    /**
     * 创建新记录
     */
    @PostMapping
    public ApiResponse<Record> createRecord(@RequestBody RecordRequest request,
                                   @RequestHeader("X-User-ID") String userId) {
        try {
            // 设置用户ID
            request.setUserId(userId);
            Record record = recordService.createRecord(request);
            return ApiResponse.ok(record);
        } catch (Exception e) {
            return ApiResponse.error("创建记录失败: " + e.getMessage());
        }
    }

    /**
     * 更新记录信息
     */
    @PutMapping("/{id}")
    public ApiResponse<Record> updateRecord(@PathVariable Integer id,
                                   @RequestBody RecordRequest request,
                                   @RequestHeader("X-User-ID") String userId) {
        try {
            // 设置用户ID
            request.setUserId(userId);
            Record record = recordService.updateRecord(id, request);
            if (record != null) {
                return ApiResponse.ok(record);
            } else {
                return ApiResponse.error("记录不存在或无权修改");
            }
        } catch (Exception e) {
            return ApiResponse.error("更新记录失败: " + e.getMessage());
        }
    }

    /**
     * 删除记录
     */
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteRecord(@PathVariable Integer id,
                                   @RequestHeader("X-User-ID") String userId) {
        try {
            boolean success = recordService.deleteRecord(id, userId);
            if (success) {
                return ApiResponse.ok("删除成功");
            } else {
                return ApiResponse.error("记录不存在或无权删除");
            }
        } catch (Exception e) {
            return ApiResponse.error("删除记录失败: " + e.getMessage());
        }
    }
}