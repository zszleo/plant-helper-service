package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.dto.req.ReminderPageQueryRequest;
import com.tencent.wxcloudrun.dto.req.ReminderRequest;
import com.tencent.wxcloudrun.dto.resp.PageResponse;
import com.tencent.wxcloudrun.model.Reminder;

/**
 * 提醒管理服务接口
 * @author zszleon
 */
public interface ReminderService {

    /**
     * 获取用户的所有提醒（分页）
     */
    PageResponse<Reminder> getRemindersByPage(ReminderPageQueryRequest request);

    /**
     * 根据ID获取提醒详情（需要验证用户权限）
     */
    Reminder getReminderById(Long id, String userId);

    /**
     * 创建新提醒
     */
    Reminder createReminder(ReminderRequest request);

    /**
     * 更新提醒信息（需要验证用户权限）
     */
    Reminder updateReminder(Long id, ReminderRequest request);

    /**
     * 删除提醒（需要验证用户权限）
     */
    boolean deleteReminder(Long id, String userId);

    /**
     * 启用/禁用提醒（需要验证用户权限）
     */
    Reminder toggleReminder(Long id, Boolean enabled, String userId);
}