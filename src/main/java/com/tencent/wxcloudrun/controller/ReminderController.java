package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.dto.resp.ApiResponse;
import com.tencent.wxcloudrun.dto.req.ReminderRequest;
import com.tencent.wxcloudrun.model.Reminder;
import com.tencent.wxcloudrun.service.ReminderService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 提醒管理控制器
 * @author zszleon
 */
@RestController
@RequestMapping("/api/reminders")
public class ReminderController {

    @Resource
    private ReminderService reminderService;

    /**
     * 获取用户的所有提醒
     */
    @GetMapping
    public ApiResponse<List<Reminder>> getReminders(@RequestHeader("X-User-ID") String userId) {
        try {
            List<Reminder> reminders = reminderService.getRemindersByUserId(userId);
            return ApiResponse.ok(reminders);
        } catch (Exception e) {
            return ApiResponse.error("获取提醒列表失败: " + e.getMessage());
        }
    }

    /**
     * 根据植物ID获取提醒
     */
    @GetMapping("/plant/{plantId}")
    public ApiResponse<List<Reminder>> getRemindersByPlantId(@PathVariable Integer plantId,
                                            @RequestHeader("X-User-ID") String userId) {
        try {
            List<Reminder> reminders = reminderService.getRemindersByPlantId(plantId, userId);
            return ApiResponse.ok(reminders);
        } catch (Exception e) {
            return ApiResponse.error("获取植物提醒失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID获取提醒详情
     */
    @GetMapping("/{id}")
    public ApiResponse<Reminder> getReminderById(@PathVariable Integer id,
                                      @RequestHeader("X-User-ID") String userId) {
        try {
            Reminder reminder = reminderService.getReminderById(id, userId);
            if (reminder != null) {
                return ApiResponse.ok(reminder);
            } else {
                return ApiResponse.error("提醒不存在或无权访问");
            }
        } catch (Exception e) {
            return ApiResponse.error("获取提醒详情失败: " + e.getMessage());
        }
    }

    /**
     * 创建新提醒
     */
    @PostMapping
    public ApiResponse<Reminder> createReminder(@RequestBody ReminderRequest request,
                                     @RequestHeader("X-User-ID") String userId) {
        try {
            // 设置用户ID
            request.setUserId(userId);
            Reminder reminder = reminderService.createReminder(request);
            return ApiResponse.ok(reminder);
        } catch (Exception e) {
            return ApiResponse.error("创建提醒失败: " + e.getMessage());
        }
    }

    /**
     * 更新提醒信息
     */
    @PutMapping("/{id}")
    public ApiResponse<Reminder> updateReminder(@PathVariable Integer id,
                                     @RequestBody ReminderRequest request,
                                     @RequestHeader("X-User-ID") String userId) {
        try {
            // 设置用户ID
            request.setUserId(userId);
            Reminder reminder = reminderService.updateReminder(id, request);
            if (reminder != null) {
                return ApiResponse.ok(reminder);
            } else {
                return ApiResponse.error("提醒不存在或无权修改");
            }
        } catch (Exception e) {
            return ApiResponse.error("更新提醒失败: " + e.getMessage());
        }
    }

    /**
     * 删除提醒
     */
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteReminder(@PathVariable Integer id,
                                     @RequestHeader("X-User-ID") String userId) {
        try {
            boolean success = reminderService.deleteReminder(id, userId);
            if (success) {
                return ApiResponse.ok("删除成功");
            } else {
                return ApiResponse.error("提醒不存在或无权删除");
            }
        } catch (Exception e) {
            return ApiResponse.error("删除提醒失败: " + e.getMessage());
        }
    }

    /**
     * 启用/禁用提醒
     */
    @PatchMapping("/{id}/toggle")
    public ApiResponse<Reminder> toggleReminder(@PathVariable Integer id,
                                     @RequestParam Boolean enabled,
                                     @RequestHeader("X-User-ID") String userId) {
        try {
            Reminder reminder = reminderService.toggleReminder(id, enabled, userId);
            if (reminder != null) {
                return ApiResponse.ok(reminder);
            } else {
                return ApiResponse.error("提醒不存在或无权操作");
            }
        } catch (Exception e) {
            return ApiResponse.error("切换提醒状态失败: " + e.getMessage());
        }
    }
}