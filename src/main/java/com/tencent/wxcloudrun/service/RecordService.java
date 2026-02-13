package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.dto.req.RecordPageQueryRequest;
import com.tencent.wxcloudrun.dto.req.RecordRequest;
import com.tencent.wxcloudrun.dto.resp.PageResponse;
import com.tencent.wxcloudrun.dto.resp.RecordResponse;

/**
 * 生长记录管理服务接口
 * @author zszleon
 */
public interface RecordService {

    /**
     * 获取用户的所有记录（分页）
     */
    PageResponse<RecordResponse> getRecordsByPage(RecordPageQueryRequest request);

    /**
     * 根据ID获取记录详情（需要验证用户权限）
     */
    RecordResponse getRecordById(Long id, Long userId);

    /**
     * 创建新记录
     */
    RecordResponse createRecord(RecordRequest request);

    /**
     * 更新记录信息（需要验证用户权限）
     */
    RecordResponse updateRecord(Long id, RecordRequest request);

    /**
     * 删除记录（需要验证用户权限）
     */
    boolean deleteRecord(Long id, Long userId);

    /**
     * 根据植物ID获取记录总数（需要验证用户权限）
     */
    Integer countRecordsByPlantId(Long plantId, Long userId);
}