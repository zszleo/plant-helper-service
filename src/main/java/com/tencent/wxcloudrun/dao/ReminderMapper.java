package com.tencent.wxcloudrun.dao;

import com.tencent.wxcloudrun.model.Reminder;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 提醒信息数据访问接口
 * @author zszleon
 */
@Mapper
public interface ReminderMapper {

    /**
     * 根据用户ID查询提醒列表
     */
    @Select("SELECT * FROM reminders WHERE user_id = #{userId} ORDER BY next_remind_time ASC")
    List<Reminder> findByUserId(@Param("userId") String userId);

    /**
     * 根据植物ID和用户ID查询提醒列表（用于权限验证）
     */
    @Select("SELECT r.* FROM reminders r " +
            "INNER JOIN plants p ON r.plant_id = p.id " +
            "WHERE r.plant_id = #{plantId} AND r.user_id = #{userId} " +
            "ORDER BY r.next_remind_time ASC")
    List<Reminder> findByPlantIdAndUserId(@Param("plantId") Integer plantId, 
                                         @Param("userId") String userId);

    /**
     * 根据ID和用户ID查询提醒（用于权限验证）
     */
    @Select("SELECT * FROM reminders WHERE id = #{id} AND user_id = #{userId}")
    Reminder findByIdAndUserId(@Param("id") Integer id, @Param("userId") String userId);

    /**
     * 插入新提醒
     */
    @Insert("INSERT INTO reminders (user_id, plant_id, type, custom_type, time, frequency, " +
            "frequency_type, next_remind_time, is_enabled, create_time, update_time) " +
            "VALUES (#{userId}, #{plantId}, #{type}, #{customType}, #{time}, #{frequency}, " +
            "#{frequencyType}, #{nextRemindTime}, #{isEnabled}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Reminder reminder);

    /**
     * 更新提醒信息
     */
    @Update("UPDATE reminders SET type = #{type}, custom_type = #{customType}, " +
            "time = #{time}, frequency = #{frequency}, frequency_type = #{frequencyType}, " +
            "next_remind_time = #{nextRemindTime}, is_enabled = #{isEnabled}, " +
            "update_time = NOW() WHERE id = #{id} AND user_id = #{userId}")
    int update(Reminder reminder);

    /**
     * 删除提醒
     */
    @Delete("DELETE FROM reminders WHERE id = #{id} AND user_id = #{userId}")
    int delete(@Param("id") Integer id, @Param("userId") String userId);

    /**
     * 切换提醒启用状态
     */
    @Update("UPDATE reminders SET is_enabled = #{enabled}, update_time = NOW() " +
            "WHERE id = #{id} AND user_id = #{userId}")
    int toggleEnabled(@Param("id") Integer id, 
                     @Param("userId") String userId, 
                     @Param("enabled") Boolean enabled);
}