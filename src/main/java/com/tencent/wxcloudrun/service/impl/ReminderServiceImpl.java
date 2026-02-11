package com.tencent.wxcloudrun.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.wxcloudrun.dao.PlantMapper;
import com.tencent.wxcloudrun.dao.ReminderMapper;
import com.tencent.wxcloudrun.dto.req.ReminderPageQueryRequest;
import com.tencent.wxcloudrun.dto.req.ReminderRequest;
import com.tencent.wxcloudrun.dto.resp.PageResponse;
import com.tencent.wxcloudrun.dto.resp.RecordResponse;
import com.tencent.wxcloudrun.dto.resp.ReminderResponse;
import com.tencent.wxcloudrun.exception.BusinessException;
import com.tencent.wxcloudrun.model.Plant;
import com.tencent.wxcloudrun.model.Reminder;
import com.tencent.wxcloudrun.service.ReminderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    public PageResponse<ReminderResponse> getRemindersByPage(ReminderPageQueryRequest request) {
        Long userId = request.getUserId();
        Long plantId = request.getPlantId();

        if (plantId != null) {
            Plant plant = plantMapper.findByIdAndUserId(plantId, userId);
            if (plant == null) {
                throw new BusinessException("植物不存在或无权访问");
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

        List<ReminderResponse> reminderResponses = BeanUtil.copyToList(page.getRecords(), ReminderResponse.class);

        return PageResponse.<ReminderResponse>builder()
                .list(reminderResponses)
                .total(page.getTotal())
                .pageNum(request.getPageNum())
                .pageSize(request.getPageSize())
                .totalPages((int) page.getPages())
                .build();
    }

    @Override
    public ReminderResponse getReminderById(Long id, Long userId) {
        Reminder reminder = reminderMapper.findByIdAndUserId(id, userId);
        return reminder != null ? BeanUtil.copyProperties(reminder,ReminderResponse.class) : null;
    }

    @Override
    public ReminderResponse createReminder(ReminderRequest request) {
        Plant plant = plantMapper.findByIdAndUserId(request.getPlantId(), request.getUserId());
        if (plant == null) {
            throw new BusinessException("植物不存在或无权访问");
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
        return BeanUtil.copyProperties(reminder,ReminderResponse.class);
    }

    @Override
    public ReminderResponse updateReminder(Long id, ReminderRequest request) {
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
        return result > 0 ? BeanUtil.copyProperties(existingReminder,ReminderResponse.class) : null;
    }

    @Override
    public boolean deleteReminder(Long id, Long userId) {
        QueryWrapper<Reminder> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id).eq("user_id", userId);
        int result = reminderMapper.delete(wrapper);
        return result > 0;
    }

    @Override
    public ReminderResponse toggleReminder(Long id, Boolean enabled, Long userId) {
        int result = reminderMapper.toggleEnabled(id, userId, enabled);
        if (result > 0) {
            Reminder reminder = reminderMapper.findByIdAndUserId(id, userId);
            return reminder != null ? BeanUtil.copyProperties(reminder,ReminderResponse.class) : null;
        }
        return null;
    }


    /**
     * 计算下次提醒时间
     */
    private Date calculateNextRemindTime(ReminderRequest request) {
        Calendar calendar = Calendar.getInstance();
        
        switch (request.getFrequencyType()) {
            case "daily":
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                break;
            case "weekly":
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
                break;
            case "monthly":
                calendar.add(Calendar.MONTH, 1);
                break;
            case "custom":
                int days = request.getFrequency() != null ? request.getFrequency() : 1;
                calendar.add(Calendar.DAY_OF_MONTH, days);
                break;
            default:
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                break;
        }
        
        return calendar.getTime();
    }
}