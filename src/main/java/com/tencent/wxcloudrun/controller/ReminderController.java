package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.auth.LoginUser;
import com.tencent.wxcloudrun.constant.YesOrNoEnum;
import com.tencent.wxcloudrun.dto.req.CommonRequest;
import com.tencent.wxcloudrun.dto.resp.ApiResponse;
import com.tencent.wxcloudrun.dto.resp.PageResponse;
import com.tencent.wxcloudrun.dto.req.ReminderPageQueryRequest;
import com.tencent.wxcloudrun.dto.req.ReminderRequest;
import com.tencent.wxcloudrun.model.Reminder;
import com.tencent.wxcloudrun.service.ReminderService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * 提醒管理控制器
 * @author zszleon
 */
@Tag(name = "提醒管理", description = "植物养护提醒（浇水、施肥、自定义提醒）的增删改查、分页查询等接口")
@Slf4j
@RestController
@RequestMapping("/api/reminders")
public class ReminderController {

    @Resource
    private ReminderService reminderService;

    /**
     * 获取用户的所有提醒（分页）
     */
    @Operation(summary = "获取提醒列表", description = "分页获取当前用户的提醒列表，需要Token认证")
    @SecurityRequirement(name = "TokenAuth")
    @PostMapping("/page")
    public ApiResponse<PageResponse<Reminder>> getRemindersPage(@Valid @RequestBody ReminderPageQueryRequest request,
                                                                @Parameter(hidden = true) LoginUser loginUser) {
        // 设置用户ID
        request.setUserId(loginUser.getUserId());
        log.info("获取提醒列表, 用户: {}, 页码: {}, 每页大小: {}", loginUser.getUserId(), request.getPageNum(), request.getPageSize());
        PageResponse<Reminder> pageData = reminderService.getRemindersByPage(request);
        return ApiResponse.pageOk(pageData);
    }

    /**
     * 根据ID获取提醒详情
     */
    @Operation(summary = "获取提醒详情", description = "根据提醒ID获取提醒详细信息，需要Token认证")
    @SecurityRequirement(name = "TokenAuth")
    @PostMapping("/getReminderById")
    public ApiResponse<Reminder> getReminderById(@RequestBody CommonRequest.Id id,
                                                 @Parameter(hidden = true) LoginUser loginUser) {
        log.info("获取提醒详情, 用户: {}, 提醒ID: {}", loginUser.getUserId(), id.getId());
        Reminder reminder = reminderService.getReminderById(id.getId(), loginUser.getUserId());
        if (reminder != null) {
            return ApiResponse.ok(reminder);
        } else {
            return ApiResponse.error("提醒不存在或无权访问");
        }
    }

    /**
     * 创建新提醒
     */
    @Operation(summary = "创建提醒", description = "创建新的养护提醒，需要Token认证")
    @SecurityRequirement(name = "TokenAuth")
    @PostMapping("/createReminder")
    public ApiResponse<Reminder> createReminder(@RequestBody ReminderRequest request,
                                                @Parameter(hidden = true) LoginUser loginUser) {
        log.info("创建提醒, 用户: {}, 植物ID: {}, 提醒类型: {}", loginUser.getUserId(), request.getPlantId(), request.getType());

        // 设置用户ID
        request.setUserId(loginUser.getUserId());
        Reminder reminder = reminderService.createReminder(request);
        return ApiResponse.ok(reminder);
    }

    /**
     * 更新提醒信息
     */
    @Operation(summary = "更新提醒", description = "更新提醒信息，需要Token认证")
    @SecurityRequirement(name = "TokenAuth")
    @PostMapping("/update")
    public ApiResponse<Reminder> updateReminder(@RequestBody ReminderRequest request,
                                                @Parameter(hidden = true) LoginUser loginUser) {
        log.info("更新提醒, 用户: {}, 提醒ID: {}, 提醒类型: {}", loginUser.getUserId(), request.getId(), request.getType());
        // 设置用户ID
        request.setUserId(loginUser.getUserId());
        Reminder reminder = reminderService.updateReminder(request.getId(), request);
        if (reminder != null) {
            return ApiResponse.ok(reminder);
        } else {
            return ApiResponse.error("提醒不存在或无权修改");
        }
    }

    /**
     * 删除提醒
     */
    @Operation(summary = "删除提醒", description = "删除指定提醒，需要Token认证")
    @SecurityRequirement(name = "TokenAuth")
    @PostMapping("/delete")
    public ApiResponse<String> deleteReminder(@RequestBody CommonRequest.Id id,
                                              @Parameter(hidden = true) LoginUser loginUser) {
        log.info("删除提醒, 用户: {}, 提醒ID: {}", loginUser.getUserId(), id.getId());
        boolean success = reminderService.deleteReminder(id.getId(), loginUser.getUserId());
        if (success) {
            return ApiResponse.ok("删除成功");
        } else {
            return ApiResponse.error("提醒不存在或无权删除");
        }
    }

    /**
     * 启用/禁用提醒
     */
    @Operation(summary = "切换提醒状态", description = "启用或禁用提醒功能，需要Token认证")
    @SecurityRequirement(name = "TokenAuth")
    @PostMapping("/isEnabled")
    public ApiResponse<Reminder> isEnabled(@RequestBody CommonRequest.IsEnabled enabled,
                                           @Parameter(hidden = true) LoginUser loginUser) {
        log.info("切换提醒状态, 用户: {}, 提醒ID: {}, 启用状态: {}", loginUser.getUserId(), enabled.getId(), enabled.getIsEnabled());
        Reminder reminder = reminderService.toggleReminder(enabled.getId(),
                YesOrNoEnum.YES.getCode().equals(enabled.getIsEnabled()),
                loginUser.getUserId());
        if (reminder != null) {
            return ApiResponse.ok(reminder);
        } else {
            return ApiResponse.error("提醒不存在或无权操作");
        }
    }
}