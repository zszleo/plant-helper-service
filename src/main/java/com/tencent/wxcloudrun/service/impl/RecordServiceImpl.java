package com.tencent.wxcloudrun.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tencent.wxcloudrun.dao.PlantMapper;
import com.tencent.wxcloudrun.dao.RecordMapper;
import com.tencent.wxcloudrun.dto.req.RecordPageQueryRequest;
import com.tencent.wxcloudrun.dto.req.RecordRequest;
import com.tencent.wxcloudrun.dto.resp.PageResponse;
import com.tencent.wxcloudrun.dto.resp.PlantResponse;
import com.tencent.wxcloudrun.dto.resp.RecordResponse;
import com.tencent.wxcloudrun.exception.BusinessException;
import com.tencent.wxcloudrun.model.Plant;
import com.tencent.wxcloudrun.model.Record;
import com.tencent.wxcloudrun.service.RecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 生长记录管理服务实现类
 * @author zszleon
 */
@Service
public class RecordServiceImpl implements RecordService {

    @Resource
    private RecordMapper recordMapper;
    @Resource
    private PlantMapper plantMapper;

    @Override
    public PageResponse<RecordResponse> getRecordsByPage(RecordPageQueryRequest request) {
        Long userId = request.getUserId();
        Long plantId = request.getPlantId();

        if (plantId != null) {
            Plant plant = plantMapper.findByIdAndUserId(plantId, userId);
            if (plant == null) {
                throw new BusinessException("植物不存在或无权访问");
            }
        }

        Page<Record> page = new Page<>(request.getPageNum().longValue(), request.getPageSize().longValue());
        QueryWrapper<Record> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        if (plantId != null) {
            wrapper.eq("plant_id", plantId);
        }
        wrapper.orderByDesc("record_time");
        recordMapper.selectPage(page, wrapper);

        List<RecordResponse> recordResponses = BeanUtil.copyToList(page.getRecords(), RecordResponse.class);


        return PageResponse.<RecordResponse>builder()
                .list(recordResponses)
                .total(page.getTotal())
                .pageNum(request.getPageNum())
                .pageSize(request.getPageSize())
                .totalPages((int) page.getPages())
                .build();
    }

    @Override
    public RecordResponse getRecordById(Long id, Long userId) {
        Record record = recordMapper.findByIdAndUserId(id, userId);
        return record != null ? BeanUtil.copyProperties(record, RecordResponse.class) : null;
    }

    @Override
    public RecordResponse createRecord(RecordRequest request) {
        Plant plant = plantMapper.findByIdAndUserId(request.getPlantId(), request.getUserId());
        if (plant == null) {
            throw new BusinessException("植物不存在或无权访问");
        }

        Record record = new Record();
        record.setUserId(request.getUserId());
        record.setPlantId(request.getPlantId());
        record.setType(request.getType());
        record.setRecordTime(request.getRecordTime() != null ? request.getRecordTime() : new Date());
        record.setNotes(request.getNotes());
        record.setImageUrl(request.getImageUrl());

        recordMapper.insert(record);
        return BeanUtil.copyProperties(record, RecordResponse.class);
    }

    @Override
    public RecordResponse updateRecord(Long id, RecordRequest request) {
        Record existingRecord = recordMapper.findByIdAndUserId(id, request.getUserId());
        if (existingRecord == null) {
            return null;
        }

        existingRecord.setType(request.getType());
        existingRecord.setRecordTime(request.getRecordTime());
        existingRecord.setNotes(request.getNotes());
        existingRecord.setImageUrl(request.getImageUrl());

        int result = recordMapper.updateById(existingRecord);
        return result > 0 ? BeanUtil.copyProperties(existingRecord, RecordResponse.class) : null;
    }

    @Override
    public boolean deleteRecord(Long id, Long userId) {
        QueryWrapper<Record> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id).eq("user_id", userId);
        int result = recordMapper.delete(wrapper);
        return result > 0;
    }

    @Override
    public Integer countRecordsByPlantId(Long plantId, Long userId) {
        QueryWrapper<Record> wrapper = new QueryWrapper<>();
        wrapper.eq("plant_id", plantId).eq("user_id", userId);
        return Math.toIntExact(recordMapper.selectCount(wrapper));
    }
}