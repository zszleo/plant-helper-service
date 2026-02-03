package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.dao.PlantMapper;
import com.tencent.wxcloudrun.dto.req.PlantRequest;
import com.tencent.wxcloudrun.model.Plant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * PlantService 单元测试类
 */
@ExtendWith(MockitoExtension.class)
class PlantServiceImplTest {

    @Mock
    private PlantMapper plantMapper;

    @InjectMocks
    private PlantServiceImpl plantService;

    private PlantRequest plantRequest;
    private Plant existingPlant;

    @BeforeEach
    void setUp() {
        // 准备测试数据
        plantRequest = new PlantRequest();
        plantRequest.setUserId("test_user_001");
        plantRequest.setName("测试植物");
        plantRequest.setType("succulent");
        plantRequest.setDescription("测试描述");
        plantRequest.setPlantDate(LocalDateTime.now());
        plantRequest.setStatus("healthy");
        plantRequest.setWateringInterval(7);
        plantRequest.setFertilizingInterval(30);

        existingPlant = new Plant();
        existingPlant.setId(1);
        existingPlant.setUserId("test_user_001");
        existingPlant.setName("现有植物");
        existingPlant.setType("ivy");
        existingPlant.setDescription("现有描述");
        existingPlant.setPlantDate(LocalDateTime.now());
    }

    @Test
    void testCreatePlant_Success() {
        // Given - 准备测试数据
        doAnswer(invocation -> {
            Plant plant = invocation.getArgument(0);
            plant.setId(1);
            return null;
        }).when(plantMapper).insert(any(Plant.class));

        // When - 执行测试方法
        Plant result = plantService.createPlant(plantRequest);

        // Then - 验证结果
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("test_user_001", result.getUserId());
        assertEquals("测试植物", result.getName());
        assertEquals("succulent", result.getType());
        assertEquals("healthy", result.getStatus());
        assertEquals(7, result.getWateringInterval());
        assertEquals(30, result.getFertilizingInterval());
        
        verify(plantMapper, times(1)).insert(any(Plant.class));
    }

    @Test
    void testGetPlantById_Success() {
        // Given
        when(plantMapper.findByIdAndUserId(1, "test_user_001")).thenReturn(existingPlant);

        // When
        Plant result = plantService.getPlantById(1, "test_user_001");

        // Then
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("test_user_001", result.getUserId());
        verify(plantMapper, times(1)).findByIdAndUserId(1, "test_user_001");
    }

    @Test
    void testGetPlantById_NotFound() {
        // Given
        when(plantMapper.findByIdAndUserId(999, "test_user_001")).thenReturn(null);

        // When
        Plant result = plantService.getPlantById(999, "test_user_001");

        // Then
        assertNull(result);
        verify(plantMapper, times(1)).findByIdAndUserId(999, "test_user_001");
    }

    @Test
    void testUpdatePlant_Success() {
        // Given
        when(plantMapper.findByIdAndUserId(1, "test_user_001")).thenReturn(existingPlant);
        when(plantMapper.update(any(Plant.class))).thenReturn(1);

        // When
        Plant result = plantService.updatePlant(1, plantRequest);

        // Then
        assertNotNull(result);
        assertEquals("测试植物", result.getName());
        assertEquals("succulent", result.getType());
        verify(plantMapper, times(1)).findByIdAndUserId(1, "test_user_001");
        verify(plantMapper, times(1)).update(any(Plant.class));
    }

    @Test
    void testUpdatePlant_NotFound() {
        // Given
        when(plantMapper.findByIdAndUserId(999, "test_user_001")).thenReturn(null);

        // When
        Plant result = plantService.updatePlant(999, plantRequest);

        // Then
        assertNull(result);
        verify(plantMapper, times(1)).findByIdAndUserId(999, "test_user_001");
        verify(plantMapper, never()).update(any(Plant.class));
    }

    @Test
    void testDeletePlant_Success() {
        // Given
        when(plantMapper.delete(1, "test_user_001")).thenReturn(1);

        // When
        boolean result = plantService.deletePlant(1, "test_user_001");

        // Then
        assertTrue(result);
        verify(plantMapper, times(1)).delete(1, "test_user_001");
    }

    @Test
    void testDeletePlant_NotFound() {
        // Given
        when(plantMapper.delete(999, "test_user_001")).thenReturn(0);

        // When
        boolean result = plantService.deletePlant(999, "test_user_001");

        // Then
        assertFalse(result);
        verify(plantMapper, times(1)).delete(999, "test_user_001");
    }
}