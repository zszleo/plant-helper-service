package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.dao.PlantMapper;
import com.tencent.wxcloudrun.dao.ReminderMapper;
import com.tencent.wxcloudrun.dto.req.ReminderRequest;
import com.tencent.wxcloudrun.model.Plant;
import com.tencent.wxcloudrun.model.Reminder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * ReminderService 单元测试类
 */
@ExtendWith(MockitoExtension.class)
class ReminderServiceImplTest {

    @Mock
    private ReminderMapper reminderMapper;

    @Mock
    private PlantMapper plantMapper;

    @InjectMocks
    private ReminderServiceImpl reminderService;

    private ReminderRequest reminderRequest;
    private Plant existingPlant;
    private Reminder existingReminder;

    @BeforeEach
    void setUp() {
        // 准备测试数据
        reminderRequest = new ReminderRequest();
        reminderRequest.setUserId("test_user_001");
        reminderRequest.setPlantId("1");
        reminderRequest.setType("watering");
        reminderRequest.setTime("09:00");
        reminderRequest.setFrequency(7);
        reminderRequest.setFrequencyType("weekly");
        reminderRequest.setIsEnabled(true);

        existingPlant = new Plant();
        existingPlant.setId(1);
        existingPlant.setUserId("test_user_001");
        existingPlant.setName("测试植物");

        existingReminder = new Reminder();
        existingReminder.setId(1);
        existingReminder.setUserId("test_user_001");
        existingReminder.setPlantId("1");
        existingReminder.setType("watering");
        existingReminder.setTime("09:00");
        existingReminder.setFrequency(7);
        existingReminder.setFrequencyType("weekly");
        existingReminder.setIsEnabled(true);
    }

    @Test
    void testCreateReminder_Success() {
        // Given
        when(plantMapper.findByIdAndUserId(1, "test_user_001")).thenReturn(existingPlant);
        doAnswer(invocation -> {
            Reminder reminder = invocation.getArgument(0);
            reminder.setId(1);
            return null;
        }).when(reminderMapper).insert(any(Reminder.class));

        // When
        Reminder result = reminderService.createReminder(reminderRequest);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("test_user_001", result.getUserId());
        assertEquals("1", result.getPlantId());
        assertEquals("watering", result.getType());
        assertEquals("09:00", result.getTime());
        assertEquals(7, result.getFrequency());
        assertEquals("weekly", result.getFrequencyType());
        assertTrue(result.getIsEnabled());
        
        verify(plantMapper, times(1)).findByIdAndUserId(1, "test_user_001");
        verify(reminderMapper, times(1)).insert(any(Reminder.class));
    }

    @Test
    void testToggleReminder_Success() {
        // Given
        when(reminderMapper.toggleEnabled(1, "test_user_001", false)).thenReturn(1);
        when(reminderMapper.findByIdAndUserId(1, "test_user_001")).thenReturn(existingReminder);

        // When
        Reminder result = reminderService.toggleReminder(1, false, "test_user_001");

        // Then
        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(reminderMapper, times(1)).toggleEnabled(1, "test_user_001", false);
        verify(reminderMapper, times(1)).findByIdAndUserId(1, "test_user_001");
    }

    @Test
    void testGetRemindersByPlantId_Success() {
        // Given
        List<Reminder> expectedReminders = Arrays.asList(existingReminder);
        when(plantMapper.findByIdAndUserId(1, "test_user_001")).thenReturn(existingPlant);
        when(reminderMapper.findByPlantIdAndUserId(1, "test_user_001")).thenReturn(expectedReminders);

        // When
        List<Reminder> result = reminderService.getRemindersByPlantId(1, "test_user_001");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
        
        verify(plantMapper, times(1)).findByIdAndUserId(1, "test_user_001");
        verify(reminderMapper, times(1)).findByPlantIdAndUserId(1, "test_user_001");
    }

    @Test
    void testCalculateNextRemindTime_Daily() {
        // Given
        reminderRequest.setFrequencyType("daily");
        
        // When - 通过反射调用私有方法
        LocalDateTime result = invokeCalculateNextRemindTime(reminderRequest);
        
        // Then
        assertNotNull(result);
        assertTrue(result.isAfter(LocalDateTime.now()));
    }

    @Test
    void testCalculateNextRemindTime_Weekly() {
        // Given
        reminderRequest.setFrequencyType("weekly");
        
        // When
        LocalDateTime result = invokeCalculateNextRemindTime(reminderRequest);
        
        // Then
        assertNotNull(result);
        assertTrue(result.isAfter(LocalDateTime.now()));
    }

    @Test
    void testCalculateNextRemindTime_Monthly() {
        // Given
        reminderRequest.setFrequencyType("monthly");
        
        // When
        LocalDateTime result = invokeCalculateNextRemindTime(reminderRequest);
        
        // Then
        assertNotNull(result);
        assertTrue(result.isAfter(LocalDateTime.now()));
    }

    @Test
    void testCalculateNextRemindTime_Custom() {
        // Given
        reminderRequest.setFrequencyType("custom");
        reminderRequest.setFrequency(5);
        
        // When
        LocalDateTime result = invokeCalculateNextRemindTime(reminderRequest);
        
        // Then
        assertNotNull(result);
        assertTrue(result.isAfter(LocalDateTime.now()));
    }

    @Test
    void testDeleteReminder_Success() {
        // Given
        when(reminderMapper.delete(1, "test_user_001")).thenReturn(1);

        // When
        boolean result = reminderService.deleteReminder(1, "test_user_001");

        // Then
        assertTrue(result);
        verify(reminderMapper, times(1)).delete(1, "test_user_001");
    }

    // 辅助方法：通过反射调用私有方法
    private LocalDateTime invokeCalculateNextRemindTime(ReminderRequest request) {
        try {
            java.lang.reflect.Method method = ReminderServiceImpl.class.getDeclaredMethod("calculateNextRemindTime", ReminderRequest.class);
            method.setAccessible(true);
            return (LocalDateTime) method.invoke(reminderService, request);
        } catch (Exception e) {
            throw new RuntimeException("调用私有方法失败", e);
        }
    }
}