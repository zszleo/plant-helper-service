package com.tencent.wxcloudrun.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tencent.wxcloudrun.model.Reminder;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 提醒信息数据访问接口
 * @author zszleon
 */
@Mapper
public interface ReminderMapper extends BaseMapper<Reminder> {

    /**
     * 根据用户ID查询提醒列表
     */
    @Select("SELECT * FROM reminders WHERE user_id = #{userId} ORDER BY next_remind_time ASC")
    List<Reminder> findByUserId(@Param("userId") Long userId);

    /**
     * 根据植物ID和用户ID查询提醒列表（用于权限验证）
     */
    @Select("SELECT r.* FROM reminders r " +
            "INNER JOIN plants p ON r.plant_id = p.id " +
            "WHERE r.plant_id = #{plantId} AND r.user_id = #{userId} " +
            "ORDER BY r.next_remind_time ASC")
    List<Reminder> findByPlantIdAndUserId(@Param("plantId") Long plantId, 
                                         @Param("userId") Long userId);

    /**
     * 根据ID和用户ID查询提醒（用于权限验证）
     */
    @Select("SELECT * FROM reminders WHERE id = #{id} AND user_id = #{userId}")
    Reminder findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    /**
     * 切换提醒启用状态
     */
    @Update("UPDATE reminders SET is_enabled = #{enabled}, update_time = NOW() " +
            "WHERE id = #{id} AND user_id = #{userId}")
    int toggleEnabled(@Param("id") Long id, 
                     @Param("userId") Long userId,
                     @Param("enabled") Boolean enabled);
}