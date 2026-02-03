package com.tencent.wxcloudrun.service.integration;

import com.tencent.wxcloudrun.dao.PlantMapper;
import com.tencent.wxcloudrun.model.Plant;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 数据库集成测试类
 */
@SpringBootTest
@Transactional
@MapperScan(basePackages = {"com.tencent.wxcloudrun.dao"})
class DatabaseIntegrationTest {

    @Autowired
    private PlantMapper plantMapper;

    @Test
    void testPlantMapper_InsertAndFind() {
        // Given - 准备测试数据
        Plant plant = new Plant();
        plant.setUserId("integration_test_user");
        plant.setName("集成测试植物");
        plant.setType("succulent");
        plant.setDescription("集成测试描述");
        plant.setPlantDate(LocalDateTime.now());
        plant.setStatus("healthy");
        plant.setWateringInterval(7);
        plant.setFertilizingInterval(30);

        // When - 执行插入操作
        plantMapper.insert(plant);
        
        // Then - 验证插入成功并可以查询到
        assertNotNull(plant.getId(), "插入后应该生成ID");
        
        Plant foundPlant = plantMapper.findByIdAndUserId(plant.getId(), "integration_test_user");
        assertNotNull(foundPlant, "应该能查询到插入的植物");
        assertEquals("集成测试植物", foundPlant.getName());
        assertEquals("succulent", foundPlant.getType());
        assertEquals("healthy", foundPlant.getStatus());
    }

    @Test
    void testPlantMapper_Update() {
        // Given - 先插入一条记录
        Plant plant = new Plant();
        plant.setUserId("integration_test_user");
        plant.setName("原始名称");
        plant.setType("succulent");
        plant.setPlantDate(LocalDateTime.now());
        plant.setStatus("healthy");
        
        plantMapper.insert(plant);
        Integer plantId = plant.getId();
        
        // When - 更新记录
        plant.setName("更新后的名称");
        plant.setStatus("growing");
        int updateResult = plantMapper.update(plant);
        
        // Then - 验证更新结果
        assertEquals(1, updateResult, "应该成功更新一条记录");
        
        Plant updatedPlant = plantMapper.findByIdAndUserId(plantId, "integration_test_user");
        assertNotNull(updatedPlant);
        assertEquals("更新后的名称", updatedPlant.getName());
        assertEquals("growing", updatedPlant.getStatus());
    }

    @Test
    void testPlantMapper_Delete() {
        // Given - 先插入一条记录
        Plant plant = new Plant();
        plant.setUserId("integration_test_user");
        plant.setName("待删除植物");
        plant.setType("succulent");
        plant.setPlantDate(LocalDateTime.now());
        plant.setStatus("healthy");
        
        plantMapper.insert(plant);
        Integer plantId = plant.getId();
        
        // When - 删除记录
        int deleteResult = plantMapper.delete(plantId, "integration_test_user");
        
        // Then - 验证删除结果
        assertEquals(1, deleteResult, "应该成功删除一条记录");
        
        Plant deletedPlant = plantMapper.findByIdAndUserId(plantId, "integration_test_user");
        assertNull(deletedPlant, "删除后应该查询不到记录");
    }

    @Test
    void testPlantMapper_FindByUserId() {
        // Given - 插入多条记录
        Plant plant1 = new Plant();
        plant1.setUserId("multi_test_user");
        plant1.setName("植物1");
        plant1.setType("succulent");
        plant1.setPlantDate(LocalDateTime.now());
        plant1.setStatus("healthy");
        
        Plant plant2 = new Plant();
        plant2.setUserId("multi_test_user");
        plant2.setName("植物2");
        plant2.setType("ivy");
        plant2.setPlantDate(LocalDateTime.now());
        plant2.setStatus("growing");
        
        plantMapper.insert(plant1);
        plantMapper.insert(plant2);
        
        // When - 按用户ID查询
        List<Plant> plants = plantMapper.findByUserId("multi_test_user");
        
        // Then - 验证查询结果
        assertNotNull(plants);
        assertTrue(plants.size() >= 2, "应该至少返回2条记录");
        
        // 验证排序（按创建时间倒序）
        for (int i = 1; i < plants.size(); i++) {
            assertTrue(plants.get(i-1).getCreateTime().isAfter(plants.get(i).getCreateTime()) ||
                      plants.get(i-1).getCreateTime().equals(plants.get(i).getCreateTime()));
        }
    }
}