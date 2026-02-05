package com.tencent.wxcloudrun.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tencent.wxcloudrun.dao.PlantMapper;
import com.tencent.wxcloudrun.dao.RecordMapper;
import com.tencent.wxcloudrun.dto.req.RecordPageQueryRequest;
import com.tencent.wxcloudrun.dto.req.RecordRequest;
import com.tencent.wxcloudrun.dto.resp.PageResponse;
import com.tencent.wxcloudrun.model.Plant;
import com.tencent.wxcloudrun.model.Record;
import com.tencent.wxcloudrun.service.RecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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
    public PageResponse<Record> getRecordsByPage(RecordPageQueryRequest request) {
        String userId = request.getUserId();
        Long plantId = request.getPlantId();

        if (plantId != null) {
            Plant plant = plantMapper.findByIdAndUserId(plantId, userId);
            if (plant == null) {
                throw new RuntimeException("植物不存在或无权访问");
            }
        }

        PageHelper.startPage(request.getPageNum(), request.getPageSize());
        List<Record> list;
        if (plantId != null) {
            list = recordMapper.findByPlantIdAndUserId(plantId, userId);
        } else {
            list = recordMapper.findByUserId(userId);
        }
        Page<Record> page = (Page<Record>) list;

        return PageResponse.<Record>builder()
                .list(page.getResult())
                .total(page.getTotal())
                .pageNum(request.getPageNum())
                .pageSize(request.getPageSize())
                .totalPages(page.getPages())
                .build();
    }

    @Override
    public Record getRecordById(Long id, String userId) {
        return recordMapper.findByIdAndUserId(id, userId);
    }

    @Override
    public Record createRecord(RecordRequest request) {
        Plant plant = plantMapper.findByIdAndUserId(request.getPlantId(), request.getUserId());
        if (plant == null) {
            throw new RuntimeException("植物不存在或无权访问");
        }

        Record record = new Record();
        record.setUserId(request.getUserId());
        record.setPlantId(request.getPlantId());
        record.setType(request.getType());
        record.setRecordTime(request.getRecordTime() != null ? request.getRecordTime() : java.time.LocalDateTime.now());
        record.setNotes(request.getNotes());
        record.setImageUrl(request.getImageUrl());

        recordMapper.insert(record);
        return record;
    }

    @Override
    public Record updateRecord(Long id, RecordRequest request) {
        Record existingRecord = recordMapper.findByIdAndUserId(id, request.getUserId());
        if (existingRecord == null) {
            return null;
        }

        existingRecord.setType(request.getType());
        existingRecord.setRecordTime(request.getRecordTime());
        existingRecord.setNotes(request.getNotes());
        existingRecord.setImageUrl(request.getImageUrl());

        int result = recordMapper.updateById(existingRecord);
        return result > 0 ? existingRecord : null;
    }

    @Override
    public boolean deleteRecord(Long id, String userId) {
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Record> wrapper = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        wrapper.eq("id", id).eq("user_id", userId);
        int result = recordMapper.delete(wrapper);
        return result > 0;
    }
}