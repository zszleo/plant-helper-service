package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.dto.req.RecordPageQueryRequest;
import com.tencent.wxcloudrun.dto.req.RecordRequest;
import com.tencent.wxcloudrun.dto.resp.PageResponse;
import com.tencent.wxcloudrun.model.Record;

/**
 * 生长记录管理服务接口
 * @author zszleon
 */
public interface RecordService {

    /**
     * 获取用户的所有记录（分页）
     */
    PageResponse<Record> getRecordsByPage(RecordPageQueryRequest request);

    /**
     * 根据ID获取记录详情（需要验证用户权限）
     */
    Record getRecordById(Long id, Long userId);

    /**
     * 创建新记录
     */
    Record createRecord(RecordRequest request);

    /**
     * 更新记录信息（需要验证用户权限）
     */
    Record updateRecord(Long id, RecordRequest request);

    /**
     * 删除记录（需要验证用户权限）
     */
    boolean deleteRecord(Long id, Long userId);
}