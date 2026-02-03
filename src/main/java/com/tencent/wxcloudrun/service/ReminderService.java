package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.dto.req.ReminderRequest;
import com.tencent.wxcloudrun.model.Reminder;

import java.util.List;

/**
 * 提醒管理服务接口
 * @author zszleon
 */
public interface ReminderService {

    /**
     * 根据用户ID获取提醒列表
     */
    List<Reminder> getRemindersByUserId(String userId);

    /**
     * 根据植物ID获取提醒列表（需要验证用户权限）
     */
    List<Reminder> getRemindersByPlantId(Integer plantId, String userId);

    /**
     * 根据ID获取提醒详情（需要验证用户权限）
     */
    Reminder getReminderById(Integer id, String userId);

    /**
     * 创建新提醒
     */
    Reminder createReminder(ReminderRequest request);

    /**
     * 更新提醒信息（需要验证用户权限）
     */
    Reminder updateReminder(Integer id, ReminderRequest request);

    /**
     * 删除提醒（需要验证用户权限）
     */
    boolean deleteReminder(Integer id, String userId);

    /**
     * 启用/禁用提醒（需要验证用户权限）
     */
    Reminder toggleReminder(Integer id, Boolean enabled, String userId);
}