package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.dao.PlantMapper;
import com.tencent.wxcloudrun.dao.RecordMapper;
import com.tencent.wxcloudrun.dto.req.RecordRequest;
import com.tencent.wxcloudrun.model.Plant;
import com.tencent.wxcloudrun.model.Record;
import com.tencent.wxcloudrun.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Record> getRecordsByUserId(String userId) {
        return recordMapper.findByUserId(userId);
    }

    @Override
    public List<Record> getRecordsByPlantId(Integer plantId, String userId) {
        // 先验证植物是否存在且属于该用户
        Plant plant = plantMapper.findByIdAndUserId(plantId, userId);
        if (plant == null) {
            throw new RuntimeException("植物不存在或无权访问");
        }
        return recordMapper.findByPlantIdAndUserId(plantId, userId);
    }

    @Override
    public Record getRecordById(Integer id, String userId) {
        return recordMapper.findByIdAndUserId(id, userId);
    }

    @Override
    public Record createRecord(RecordRequest request) {
        // 验证植物是否存在且属于该用户
        Plant plant = plantMapper.findByIdAndUserId(Integer.parseInt(request.getPlantId()), request.getUserId());
        if (plant == null) {
            throw new RuntimeException("植物不存在或无权访问");
        }
        
        Record record = new Record();
        // 设置基本属性
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
    public Record updateRecord(Integer id, RecordRequest request) {
        // 先验证权限
        Record existingRecord = recordMapper.findByIdAndUserId(id, request.getUserId());
        if (existingRecord == null) {
            return null;
        }
        
        // 更新属性
        existingRecord.setType(request.getType());
        existingRecord.setRecordTime(request.getRecordTime());
        existingRecord.setNotes(request.getNotes());
        existingRecord.setImageUrl(request.getImageUrl());
        
        int result = recordMapper.update(existingRecord);
        return result > 0 ? existingRecord : null;
    }

    @Override
    public boolean deleteRecord(Integer id, String userId) {
        int result = recordMapper.delete(id, userId);
        return result > 0;
    }
}