package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.Plant;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 植物信息数据访问接口
 * @author zszleon
 */
@Mapper
public interface PlantMapper {

    /**
     * 根据用户ID查询植物列表
     */
    @Select("SELECT * FROM plants WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<Plant> findByUserId(@Param("userId") String userId);

    /**
     * 根据ID和用户ID查询植物（用于权限验证）
     */
    @Select("SELECT * FROM plants WHERE id = #{id} AND user_id = #{userId}")
    Plant findByIdAndUserId(@Param("id") Integer id, @Param("userId") String userId);

    /**
     * 插入新植物
     */
    @Insert("INSERT INTO plants (user_id, name, type, description, image_url, plant_date, status, " +
            "watering_interval, fertilizing_interval, last_watering, last_fertilizing, create_time, update_time) " +
            "VALUES (#{userId}, #{name}, #{type}, #{description}, #{imageUrl}, #{plantDate}, #{status}, " +
            "#{wateringInterval}, #{fertilizingInterval}, #{lastWatering}, #{lastFertilizing}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Plant plant);

    /**
     * 更新植物信息
     */
    @Update("UPDATE plants SET name = #{name}, type = #{type}, description = #{description}, " +
            "image_url = #{imageUrl}, plant_date = #{plantDate}, status = #{status}, " +
            "watering_interval = #{wateringInterval}, fertilizing_interval = #{fertilizingInterval}, " +
            "last_watering = #{lastWatering}, last_fertilizing = #{lastFertilizing}, " +
            "update_time = NOW() WHERE id = #{id} AND user_id = #{userId}")
    int update(Plant plant);

    /**
     * 删除植物
     */
    @Delete("DELETE FROM plants WHERE id = #{id} AND user_id = #{userId}")
    int delete(@Param("id") Integer id, @Param("userId") String userId);
}