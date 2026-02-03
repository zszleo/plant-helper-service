package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.Record;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 生长记录数据访问接口
 * @author zszleon
 */
@Mapper
public interface RecordMapper {

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
    List<Record> findByPlantIdAndUserId(@Param("plantId") Integer plantId, 
                                       @Param("userId") String userId);

    /**
     * 根据ID和用户ID查询记录（用于权限验证）
     */
    @Select("SELECT * FROM records WHERE id = #{id} AND user_id = #{userId}")
    Record findByIdAndUserId(@Param("id") Integer id, @Param("userId") String userId);

    /**
     * 插入新记录
     */
    @Insert("INSERT INTO records (user_id, plant_id, type, record_time, notes, image_url, create_time) " +
            "VALUES (#{userId}, #{plantId}, #{type}, #{recordTime}, #{notes}, #{imageUrl}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Record record);

    /**
     * 更新记录信息
     */
    @Update("UPDATE records SET type = #{type}, record_time = #{recordTime}, " +
            "notes = #{notes}, image_url = #{imageUrl} " +
            "WHERE id = #{id} AND user_id = #{userId}")
    int update(Record record);

    /**
     * 删除记录
     */
    @Delete("DELETE FROM records WHERE id = #{id} AND user_id = #{userId}")
    int delete(@Param("id") Integer id, @Param("userId") String userId);
}