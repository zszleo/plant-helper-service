package com.tencent.wxcloudrun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.wxcloudrun.dao.PlantMapper;
import com.tencent.wxcloudrun.dao.ReminderMapper;
import com.tencent.wxcloudrun.dto.req.ReminderPageQueryRequest;
import com.tencent.wxcloudrun.dto.req.ReminderRequest;
import com.tencent.wxcloudrun.dto.resp.PageResponse;
import com.tencent.wxcloudrun.model.Plant;
import com.tencent.wxcloudrun.model.Reminder;
import com.tencent.wxcloudrun.service.ReminderService;
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
    public PageResponse<Reminder> getRemindersByPage(ReminderPageQueryRequest request) {
        Long userId = request.getUserId();
        Long plantId = request.getPlantId();

        if (plantId != null) {
            Plant plant = plantMapper.findByIdAndUserId(plantId, userId);
            if (plant == null) {
                throw new RuntimeException("植物不存在或无权访问");
            }
        }

        Page<Reminder> page = new Page<>(request.getPageNum().longValue(), request.getPageSize().longValue());
        QueryWrapper<Reminder> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        if (plantId != null) {
            wrapper.eq("plant_id", plantId);
        }
        wrapper.orderByDesc("create_time");
        reminderMapper.selectPage(page, wrapper);

        return PageResponse.<Reminder>builder()
                .list(page.getRecords())
                .total(page.getTotal())
                .pageNum(request.getPageNum())
                .pageSize(request.getPageSize())
                .totalPages((int) page.getPages())
                .build();
    }

    @Override
    public Reminder getReminderById(Long id, Long userId) {
        return reminderMapper.findByIdAndUserId(id, userId);
    }

    @Override
    public Reminder createReminder(ReminderRequest request) {
        Plant plant = plantMapper.findByIdAndUserId(request.getPlantId(), request.getUserId());
        if (plant == null) {
            throw new RuntimeException("植物不存在或无权访问");
        }

        Reminder reminder = new Reminder();
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
    public Reminder updateReminder(Long id, ReminderRequest request) {
        Reminder existingReminder = reminderMapper.findByIdAndUserId(id, request.getUserId());
        if (existingReminder == null) {
            return null;
        }

        existingReminder.setType(request.getType());
        existingReminder.setCustomType(request.getCustomType());
        existingReminder.setTime(request.getTime());
        existingReminder.setFrequency(request.getFrequency());
        existingReminder.setFrequencyType(request.getFrequencyType());
        existingReminder.setNextRemindTime(request.getNextRemindTime());
        existingReminder.setIsEnabled(request.getIsEnabled());

        int result = reminderMapper.updateById(existingReminder);
        return result > 0 ? existingReminder : null;
    }

    @Override
    public boolean deleteReminder(Long id, Long userId) {
        QueryWrapper<Reminder> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id).eq("user_id", userId);
        int result = reminderMapper.delete(wrapper);
        return result > 0;
    }

    @Override
    public Reminder toggleReminder(Long id, Boolean enabled, Long userId) {
        int result = reminderMapper.toggleEnabled(id, userId, enabled);
        if (result > 0) {
            return reminderMapper.findByIdAndUserId(id, userId);
        }
        return null;
    }

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