package com.tencent.wxcloudrun.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tencent.wxcloudrun.model.Record;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 生长记录数据访问接口
 * @author zszleon
 */
@Mapper
public interface RecordMapper extends BaseMapper<Record> {

    /**
     * 根据用户ID查询记录列表
     */
    @Select("SELECT * FROM records WHERE user_id = #{userId} ORDER BY record_time DESC")
    List<Record> findByUserId(@Param("userId") String userId);

    /**
     * 根据植物ID和用户ID查询记录列表（用于权限验证）
     */
    @Select("SELECT r.* FROM records r " +
            "INNER JOIN plants p ON r.plant_id = p.id " +
            "WHERE r.plant_id = #{plantId} AND r.user_id = #{userId} " +
            "ORDER BY r.record_time DESC")
    List<Record> findByPlantIdAndUserId(@Param("plantId") Long plantId, 
                                       @Param("userId") String userId);

    /**
     * 根据ID和用户ID查询记录（用于权限验证）
     */
    @Select("SELECT * FROM records WHERE id = #{id} AND user_id = #{userId}")
    Record findByIdAndUserId(@Param("id") Long id, @Param("userId") String userId);
}