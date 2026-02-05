package com.tencent.wxcloudrun.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tencent.wxcloudrun.model.Plant;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 植物信息数据访问接口
 * @author zszleon
 */
@Mapper
public interface PlantMapper extends BaseMapper<Plant> {

    /**
     * 根据用户ID查询植物列表
     */
    @Select("SELECT * FROM plants WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<Plant> findByUserId(@Param("userId") Long userId);

    /**
     * 根据ID和用户ID查询植物（用于权限验证）
     */
    @Select("SELECT * FROM plants WHERE id = #{id} AND user_id = #{userId}")
    Plant findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);
}