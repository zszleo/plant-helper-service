package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.dao.PlantMapper;
import com.tencent.wxcloudrun.dao.RecordMapper;
import com.tencent.wxcloudrun.dto.req.RecordRequest;
import com.tencent.wxcloudrun.model.Plant;
import com.tencent.wxcloudrun.model.Record;
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
 * RecordService 单元测试类
 */
@ExtendWith(MockitoExtension.class)
class RecordServiceImplTest {

    @Mock
    private RecordMapper recordMapper;

    @Mock
    private PlantMapper plantMapper;

    @InjectMocks
    private RecordServiceImpl recordService;

    private RecordRequest recordRequest;
    private Plant existingPlant;
    private Record existingRecord;

    @BeforeEach
    void setUp() {
        // 准备测试数据
        recordRequest = new RecordRequest();
        recordRequest.setUserId("test_user_001");
        recordRequest.setPlantId("1");
        recordRequest.setType("watering");
        recordRequest.setRecordTime(LocalDateTime.now());
        recordRequest.setNotes("测试记录");

        existingPlant = new Plant();
        existingPlant.setId(1);
        existingPlant.setUserId("test_user_001");
        existingPlant.setName("测试植物");

        existingRecord = new Record();
        existingRecord.setId(1);
        existingRecord.setUserId("test_user_001");
        existingRecord.setPlantId("1");
        existingRecord.setType("watering");
        existingRecord.setRecordTime(LocalDateTime.now());
    }

    @Test
    void testCreateRecord_Success() {
        // Given
        when(plantMapper.findByIdAndUserId(1, "test_user_001")).thenReturn(existingPlant);
        doAnswer(invocation -> {
            Record record = invocation.getArgument(0);
            record.setId(1);
            return null;
        }).when(recordMapper).insert(any(Record.class));

        // When
        Record result = recordService.createRecord(recordRequest);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("test_user_001", result.getUserId());
        assertEquals("1", result.getPlantId());
        assertEquals("watering", result.getType());
        
        verify(plantMapper, times(1)).findByIdAndUserId(1, "test_user_001");
        verify(recordMapper, times(1)).insert(any(Record.class));
    }

    @Test
    void testCreateRecord_PlantNotFound() {
        // Given
        when(plantMapper.findByIdAndUserId(999, "test_user_001")).thenReturn(null);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            recordRequest.setPlantId("999");
            recordService.createRecord(recordRequest);
        });

        assertEquals("植物不存在或无权访问", exception.getMessage());
        verify(plantMapper, times(1)).findByIdAndUserId(999, "test_user_001");
        verify(recordMapper, never()).insert(any(Record.class));
    }

    @Test
    void testGetRecordsByPlantId_Success() {
        // Given
        List<Record> expectedRecords = Arrays.asList(existingRecord);
        when(plantMapper.findByIdAndUserId(1, "test_user_001")).thenReturn(existingPlant);
        when(recordMapper.findByPlantIdAndUserId(1, "test_user_001")).thenReturn(expectedRecords);

        // When
        List<Record> result = recordService.getRecordsByPlantId(1, "test_user_001");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
        
        verify(plantMapper, times(1)).findByIdAndUserId(1, "test_user_001");
        verify(recordMapper, times(1)).findByPlantIdAndUserId(1, "test_user_001");
    }

    @Test
    void testGetRecordById_Success() {
        // Given
        when(recordMapper.findByIdAndUserId(1, "test_user_001")).thenReturn(existingRecord);

        // When
        Record result = recordService.getRecordById(1, "test_user_001");

        // Then
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("test_user_001", result.getUserId());
        verify(recordMapper, times(1)).findByIdAndUserId(1, "test_user_001");
    }

    @Test
    void testUpdateRecord_Success() {
        // Given
        when(recordMapper.findByIdAndUserId(1, "test_user_001")).thenReturn(existingRecord);
        when(recordMapper.update(any(Record.class))).thenReturn(1);

        // When
        Record result = recordService.updateRecord(1, recordRequest);

        // Then
        assertNotNull(result);
        assertEquals("watering", result.getType());
        verify(recordMapper, times(1)).findByIdAndUserId(1, "test_user_001");
        verify(recordMapper, times(1)).update(any(Record.class));
    }

    @Test
    void testDeleteRecord_Success() {
        // Given
        when(recordMapper.delete(1, "test_user_001")).thenReturn(1);

        // When
        boolean result = recordService.deleteRecord(1, "test_user_001");

        // Then
        assertTrue(result);
        verify(recordMapper, times(1)).delete(1, "test_user_001");
    }
}