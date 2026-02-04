package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.dto.req.PlantRequest;
import com.tencent.wxcloudrun.model.Plant;
import com.tencent.wxcloudrun.service.PlantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * PlantController API测试类
 */

@AutoConfigureMybatis // add
@WebMvcTest(PlantController.class)
class PlantControllerTest {

    @Resource
    private MockMvc mockMvc;

    @MockBean
    private PlantService plantService;

    private Plant testPlant;
    private PlantRequest plantRequest;

    @BeforeEach
    void setUp() {
        // 准备测试数据
        testPlant = new Plant();
        testPlant.setId(1);
        testPlant.setUserId("test_user_001");
        testPlant.setName("测试植物");
        testPlant.setType("succulent");
        testPlant.setDescription("测试描述");
        testPlant.setPlantDate(LocalDateTime.now());
        testPlant.setStatus("healthy");

        plantRequest = new PlantRequest();
        plantRequest.setName("测试植物");
        plantRequest.setType("succulent");
        plantRequest.setDescription("测试描述");
        plantRequest.setPlantDate(LocalDateTime.now());
    }

    @Test
    void testGetPlants_Success() throws Exception {
        // Given
        List<Plant> plants = Arrays.asList(testPlant);
        when(plantService.getPlantsByUserId("test_user_001")).thenReturn(plants);

        // When & Then
        mockMvc.perform(get("/api/plants")
                .header("X-User-ID", "test_user_001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].name").value("测试植物"));
    }

    @Test
    void testGetPlantById_Success() throws Exception {
        // Given
        when(plantService.getPlantById(1, "test_user_001")).thenReturn(testPlant);

        // When & Then
        mockMvc.perform(get("/api/plants/1")
                .header("X-User-ID", "test_user_001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("测试植物"));
    }

    @Test
    void testGetPlantById_NotFound() throws Exception {
        // Given
        when(plantService.getPlantById(999, "test_user_001")).thenReturn(null);

        // When & Then
        mockMvc.perform(get("/api/plants/999")
                .header("X-User-ID", "test_user_001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.message").value("植物不存在或无权访问"));
    }

    @Test
    void testCreatePlant_Success() throws Exception {
        // Given
        when(plantService.createPlant(any(PlantRequest.class))).thenReturn(testPlant);

        String requestBody = "{\"name\": \"测试植物\", \"type\": \"succulent\", \"description\": \"测试描述\", \"plantDate\": \"2024-01-01T10:00:00\"}";

        // When & Then
        mockMvc.perform(post("/api/plants")
                .header("X-User-ID", "test_user_001")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    void testUpdatePlant_Success() throws Exception {
        // Given
        when(plantService.updatePlant(eq(1), any(PlantRequest.class))).thenReturn(testPlant);

        String requestBody = "{\"name\": \"更新后的植物\", \"type\": \"succulent\", \"description\": \"更新后的描述\"}";

        // When & Then
        mockMvc.perform(put("/api/plants/1")
                .header("X-User-ID", "test_user_001")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    void testDeletePlant_Success() throws Exception {
        // Given
        when(plantService.deletePlant(1, "test_user_001")).thenReturn(true);

        // When & Then
        mockMvc.perform(delete("/api/plants/1")
                .header("X-User-ID", "test_user_001"))
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.message").value("删除成功"));
    }

    @Test
    void testMissingUserHeader() throws Exception {
        // When & Then - 缺少用户头信息
        mockMvc.perform(get("/api/plants"))
                .andExpect(status().isBadRequest());
    }
}