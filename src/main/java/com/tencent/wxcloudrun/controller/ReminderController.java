package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.dto.req.CommonRequest;
import com.tencent.wxcloudrun.dto.resp.ApiResponse;
import com.tencent.wxcloudrun.dto.resp.PageResponse;
import com.tencent.wxcloudrun.dto.req.ReminderPageQueryRequest;
import com.tencent.wxcloudrun.dto.req.ReminderRequest;
import com.tencent.wxcloudrun.model.Reminder;
import com.tencent.wxcloudrun.service.ReminderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 提醒管理控制器
 * @author zszleon
 */
@Slf4j
@RestController
@RequestMapping("/api/reminders")
public class ReminderController {

    @Resource
    private ReminderService reminderService;

    /**
     * 获取用户的所有提醒（分页）
     */
    @PostMapping("/page")
    public ApiResponse<PageResponse<Reminder>> getRemindersPage(@Valid @RequestBody ReminderPageQueryRequest request) {
        log.info("获取提醒列表, 用户: {}, 页码: {}, 每页大小: {}", request.getUserId(), request.getPageNum(), request.getPageSize());
        PageResponse<Reminder> pageData = reminderService.getRemindersByPage(request);
        return ApiResponse.pageOk(pageData);
    }

    /**
     * 根据ID获取提醒详情
     */
    @PostMapping("/getReminderById")
    public ApiResponse<Reminder> getReminderById(@RequestBody CommonRequest.Id id,
                                      @RequestHeader("X-User-ID") String userId) {
        log.info("获取提醒详情, 用户: {}, 提醒ID: {}", userId, id.getId());
        Reminder reminder = reminderService.getReminderById(id.getId(), userId);
        if (reminder != null) {
            return ApiResponse.ok(reminder);
        } else {
            return ApiResponse.error("提醒不存在或无权访问");
        }
    }

    /**
     * 创建新提醒
     */
    @PostMapping("/createReminder")
    public ApiResponse<Reminder> createReminder(@RequestBody ReminderRequest request,
                                     @RequestHeader("X-User-ID") String userId) {
        log.info("创建提醒, 用户: {}, 植物ID: {}, 提醒类型: {}", userId, request.getPlantId(), request.getType());

        // 设置用户ID
        request.setUserId(userId);
        Reminder reminder = reminderService.createReminder(request);
        return ApiResponse.ok(reminder);
    }

    /**
     * 更新提醒信息
     */
    @PostMapping("/update")
    public ApiResponse<Reminder> updateReminder(@RequestBody CommonRequest.Id id,
                                     @RequestBody ReminderRequest request,
                                     @RequestHeader("X-User-ID") String userId) {
        log.info("更新提醒, 用户: {}, 提醒ID: {}, 提醒类型: {}", userId, id.getId(), request.getType());
        // 设置用户ID
        request.setUserId(userId);
        Reminder reminder = reminderService.updateReminder(id.getId(), request);
        if (reminder != null) {
            return ApiResponse.ok(reminder);
        } else {
            return ApiResponse.error("提醒不存在或无权修改");
        }
    }

    /**
     * 删除提醒
     */
    @PostMapping("/delete")
    public ApiResponse<String> deleteReminder(@RequestBody CommonRequest.Id id,
                                     @RequestHeader("X-User-ID") String userId) {
        log.info("删除提醒, 用户: {}, 提醒ID: {}", userId, id.getId());
        boolean success = reminderService.deleteReminder(id.getId(), userId);
        if (success) {
            return ApiResponse.ok("删除成功");
        } else {
            return ApiResponse.error("提醒不存在或无权删除");
        }
    }

    /**
     * 启用/禁用提醒
     */
    @PostMapping("/{id}/toggle")
    public ApiResponse<Reminder> toggleReminder(@RequestBody CommonRequest.Id id,
                                     @RequestBody(required = false) Boolean enabled,
                                     @RequestHeader("X-User-ID") String userId) {
        log.info("切换提醒状态, 用户: {}, 提醒ID: {}, 启用状态: {}", userId, id.getId(), enabled);
        Reminder reminder = reminderService.toggleReminder(id.getId(), enabled, userId);
        if (reminder != null) {
            return ApiResponse.ok(reminder);
        } else {
            return ApiResponse.error("提醒不存在或无权操作");
        }
    }
}