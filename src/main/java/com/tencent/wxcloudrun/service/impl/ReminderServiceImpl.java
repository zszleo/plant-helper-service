package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.dao.PlantMapper;
import com.tencent.wxcloudrun.dao.ReminderMapper;
import com.tencent.wxcloudrun.dto.req.ReminderRequest;
import com.tencent.wxcloudrun.model.Plant;
import com.tencent.wxcloudrun.model.Reminder;
import com.tencent.wxcloudrun.service.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 提醒管理服务实现类
 * @author zszleon
 */
@Service
public class ReminderServiceImpl implements ReminderService {

    @Resource
    private ReminderMapper reminderMapper;
    @Resource
    private PlantMapper plantMapper;

    @Override
    public List<Reminder> getRemindersByUserId(String userId) {
        return reminderMapper.findByUserId(userId);
    }

    @Override
    public List<Reminder> getRemindersByPlantId(Integer plantId, String userId) {
        // 先验证植物是否存在且属于该用户
        Plant plant = plantMapper.findByIdAndUserId(plantId, userId);
        if (plant == null) {
            throw new RuntimeException("植物不存在或无权访问");
        }
        return reminderMapper.findByPlantIdAndUserId(plantId, userId);
    }

    @Override
    public Reminder getReminderById(Integer id, String userId) {
        return reminderMapper.findByIdAndUserId(id, userId);
    }

    @Override
    public Reminder createReminder(ReminderRequest request) {
        // 验证植物是否存在且属于该用户
        Plant plant = plantMapper.findByIdAndUserId(Integer.parseInt(request.getPlantId()), request.getUserId());
        if (plant == null) {
            throw new RuntimeException("植物不存在或无权访问");
        }
        
        Reminder reminder = new Reminder();
        // 设置基本属性
        reminder.setUserId(request.getUserId());
        reminder.setPlantId(request.getPlantId());
        reminder.setType(request.getType());
        reminder.setCustomType(request.getCustomType());
        reminder.setTime(request.getTime());
        reminder.setFrequency(request.getFrequency() != null ? request.getFrequency() : 1);
        reminder.setFrequencyType(request.getFrequencyType() != null ? request.getFrequencyType() : "daily");
        reminder.setNextRemindTime(request.getNextRemindTime() != null ? request.getNextRemindTime() : calculateNextRemindTime(request));
        reminder.setIsEnabled(request.getIsEnabled() != null ? request.getIsEnabled() : true);
        
        reminderMapper.insert(reminder);
        return reminder;
    }

    @Override
    public Reminder updateReminder(Integer id, ReminderRequest request) {
        // 先验证权限
        Reminder existingReminder = reminderMapper.findByIdAndUserId(id, request.getUserId());
        if (existingReminder == null) {
            return null;
        }
        
        // 更新属性
        existingReminder.setType(request.getType());
        existingReminder.setCustomType(request.getCustomType());
        existingReminder.setTime(request.getTime());
        existingReminder.setFrequency(request.getFrequency());
        existingReminder.setFrequencyType(request.getFrequencyType());
        existingReminder.setNextRemindTime(request.getNextRemindTime());
        existingReminder.setIsEnabled(request.getIsEnabled());
        
        int result = reminderMapper.update(existingReminder);
        return result > 0 ? existingReminder : null;
    }

    @Override
    public boolean deleteReminder(Integer id, String userId) {
        int result = reminderMapper.delete(id, userId);
        return result > 0;
    }

    @Override
    public Reminder toggleReminder(Integer id, Boolean enabled, String userId) {
        int result = reminderMapper.toggleEnabled(id, userId, enabled);
        if (result > 0) {
            return reminderMapper.findByIdAndUserId(id, userId);
        }
        return null;
    }

    /**
     * 计算下次提醒时间
     */
    private LocalDateTime calculateNextRemindTime(ReminderRequest request) {
        LocalDateTime now = LocalDateTime.now();
        
        switch (request.getFrequencyType()) {
            case "daily":
                return now.plusDays(1);
            case "weekly":
                return now.plusWeeks(1);
            case "monthly":
                return now.plusMonths(1);
            case "custom":
                return now.plusDays(request.getFrequency() != null ? request.getFrequency() : 1);
            default:
                return now.plusDays(1);
        }
    }
}