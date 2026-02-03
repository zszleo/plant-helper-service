package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.dto.req.RecordRequest;
import com.tencent.wxcloudrun.model.Record;

import java.util.List;

/**
 * 生长记录管理服务接口
 * @author zszleon
 */
public interface RecordService {

    /**
     * 根据用户ID获取记录列表
     */
    List<Record> getRecordsByUserId(String userId);

    /**
     * 根据植物ID获取记录列表（需要验证用户权限）
     */
    List<Record> getRecordsByPlantId(Integer plantId, String userId);

    /**
     * 根据ID获取记录详情（需要验证用户权限）
     */
    Record getRecordById(Integer id, String userId);

    /**
     * 创建新记录
     */
    Record createRecord(RecordRequest request);

    /**
     * 更新记录信息（需要验证用户权限）
     */
    Record updateRecord(Integer id, RecordRequest request);

    /**
     * 删除记录（需要验证用户权限）
     */
    boolean deleteRecord(Integer id, String userId);
}