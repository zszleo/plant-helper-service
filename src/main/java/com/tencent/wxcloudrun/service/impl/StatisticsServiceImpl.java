package com.tencent.wxcloudrun.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tencent.wxcloudrun.dao.PlantMapper;
import com.tencent.wxcloudrun.dao.RecordMapper;
import com.tencent.wxcloudrun.dto.req.HeatmapRequest;
import com.tencent.wxcloudrun.dto.resp.*;
import com.tencent.wxcloudrun.model.Plant;
import com.tencent.wxcloudrun.model.Record;
import com.tencent.wxcloudrun.service.StatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 统计服务实现类
 * @author zszleon
 */
@Slf4j
@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Resource
    private PlantMapper plantMapper;

    @Resource
    private RecordMapper recordMapper;

    // 植物状态映射
    private static final List<String> STATUS_LIST = CollUtil.toList("healthy", "growing", "need-care", "diseased");
    private static final List<String> TYPE_LIST = CollUtil.toList("watering", "fertilizing", "growth", "photo");

    @Override
    public PlantStatusResponse getPlantStatusStatistics(Long userId) {

        try {
            // 查询所有植物
            QueryWrapper<Plant> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId);
            List<Plant> plants = plantMapper.selectList(wrapper);
            
            int total = plants.size();
            if (total == 0) {
                return PlantStatusResponse.builder()
                        .total(0)
                        .items(Collections.emptyList())
                        .build();
            }
            
            // 按状态分组统计
            Map<String, Long> statusCountMap = plants.stream()
                    .collect(Collectors.groupingBy(Plant::getStatus, Collectors.counting()));
            
            // 构建响应项
            List<PlantStatusItem> items = new ArrayList<>();
            for (Map.Entry<String, Long> entry : statusCountMap.entrySet()) {
                String status = entry.getKey();
                Long count = entry.getValue();
                // 保留一位小数
                double percent = Math.round((count.doubleValue() / total * 100) * 10.0) / 10.0;
                
                PlantStatusItem item = PlantStatusItem.builder()
                        .status(status)
                        .count(count.intValue())
                        .percent(percent)
                        .build();
                items.add(item);
            }
            
            // 确保所有状态都有对应项（即使数量为0）
            for (String status : STATUS_LIST) {
                if (!statusCountMap.containsKey(status)) {
                    PlantStatusItem item = PlantStatusItem.builder()
                            .status(status)
                            .count(0)
                            .percent(0.0)
                            .build();
                    items.add(item);
                }
            }
            return PlantStatusResponse.builder()
                    .total(total)
                    .items(items)
                    .build();
            
        } catch (Exception e) {
            throw new RuntimeException("获取植物状态统计失败", e);
        }
    }

    @Override
    public RecordTypesResponse getRecordTypesStatistics(Long userId) {

        try {
            // 查询用户的所有记录（联查plants表确保记录有效）
            List<Record> records = recordMapper.findValidRecordsByUserId(userId);
            
            int total = records.size();
            if (total == 0) {
                return RecordTypesResponse.builder()
                        .total(0)
                        .items(Collections.emptyList())
                        .build();
            }
            
            // 按类型分组统计
            Map<String, Long> typeCountMap = records.stream()
                    .collect(Collectors.groupingBy(Record::getType, Collectors.counting()));
            
            // 构建响应项
            List<RecordTypeItem> items = new ArrayList<>();
            for (Map.Entry<String, Long> entry : typeCountMap.entrySet()) {
                String type = entry.getKey();
                Long count = entry.getValue();
                // 保留一位小数
                double percent = Math.round((count.doubleValue() / total * 100) * 10.0) / 10.0;
                
                RecordTypeItem item = RecordTypeItem.builder()
                        .type(type)
                        .count(count.intValue())
                        .percent(percent)
                        .build();
                items.add(item);
            }
            
            // 确保所有类型都有对应项（即使数量为0）
            for (String type : TYPE_LIST) {
                if (!typeCountMap.containsKey(type)) {
                    RecordTypeItem item = RecordTypeItem.builder()
                            .type(type)
                            .count(0)
                            .percent(0.0)
                            .build();
                    items.add(item);
                }
            }
            
            return RecordTypesResponse.builder()
                    .total(total)
                    .items(items)
                    .build();
            
        } catch (Exception e) {
            throw new RuntimeException("获取记录类型统计失败", e);
        }
    }

    @Override
    public HeatmapResponse getHeatmapStatistics(Long userId, HeatmapRequest request) {

        try {
            int year = request.getYear();
            int month = request.getMonth();
            
            // 获取指定月份的第一天和最后一天
            YearMonth yearMonth = YearMonth.of(year, month);
            LocalDate firstDay = yearMonth.atDay(1);
            LocalDate lastDay = yearMonth.atEndOfMonth();
            
            Date startDate = Date.from(firstDay.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date endDate = Date.from(lastDay.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
            
            // 查询该时间段内的记录（联查plants表确保记录有效）
            List<Record> records = recordMapper.findValidRecordsByUserIdAndDateRange(userId, startDate, endDate);
            
            // 按日期分组统计
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Map<String, Integer> dateCountMap = new HashMap<>();
            int totalRecords = 0;
            
            for (Record record : records) {
                String dateStr = dateFormat.format(record.getRecordTime());
                dateCountMap.put(dateStr, dateCountMap.getOrDefault(dateStr, 0) + 1);
                totalRecords++;
            }
            
            // 构建日历数据
            List<HeatmapWeekData> calendarData = buildCalendarData(year, month, dateCountMap);
            
            return HeatmapResponse.builder()
                    .year(year)
                    .month(month)
                    .totalRecords(totalRecords)
                    .calendarData(calendarData)
                    .build();
            
        } catch (Exception e) {
            throw new RuntimeException("获取热力图统计失败", e);
        }
    }
    
    /**
     * 构建日历数据
     */
    private List<HeatmapWeekData> buildCalendarData(int year, int month, Map<String, Integer> dateCountMap) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        
        // 计算日历开始日期（从周一开始）
        LocalDate calendarStart = firstDay.with(DayOfWeek.MONDAY);
        if (calendarStart.isAfter(firstDay)) {
            calendarStart = calendarStart.minusWeeks(1);
        }
        
        // 计算日历结束日期（到周日结束）
        LocalDate calendarEnd = lastDay.with(DayOfWeek.SUNDAY);
        if (calendarEnd.isBefore(lastDay)) {
            calendarEnd = calendarEnd.plusWeeks(1);
        }
        
        List<HeatmapWeekData> weekDataList = new ArrayList<>();
        LocalDate currentDate = calendarStart;
        int weekIndex = 0;
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        // 找到最大数量用于计算层级
        int maxCount = dateCountMap.values().stream()
                .max(Integer::compare)
                .orElse(1);
        
        while (!currentDate.isAfter(calendarEnd)) {
            List<HeatmapCell> cells = new ArrayList<>();
            
            // 一周7天
            for (int i = 0; i < 7; i++) {
                String dateStr = currentDate.format(dateFormatter);
                int day = currentDate.getDayOfMonth();
                boolean isCurrentMonth = currentDate.getMonthValue() == month && currentDate.getYear() == year;
                
                int count = 0;
                if (isCurrentMonth) {
                    count = dateCountMap.getOrDefault(dateStr, 0);
                }
                
                // 计算层级
                int level;
                if (!isCurrentMonth) {
                    // 非本月日期
                    level = -1;
                } else if (count == 0) {
                    // 无记录
                    level = 0;
                } else {
                    double ratio = (double) count / maxCount;
                    if (ratio <= 0.25) {
                        level = 1;
                    } else if (ratio <= 0.5) {
                        level = 2;
                    } else if (ratio <= 0.75) {
                        level = 3;
                    } else {
                        level = 4;
                    }
                }
                
                HeatmapCell cell = HeatmapCell.builder()
                        .date(dateStr)
                        .day(day)
                        .count(count)
                        .level(level)
                        .build();
                cells.add(cell);
                
                currentDate = currentDate.plusDays(1);
            }
            
            HeatmapWeekData weekData = HeatmapWeekData.builder()
                    .weekIndex(weekIndex)
                    .cells(cells)
                    .build();
            weekDataList.add(weekData);
            weekIndex++;
        }
        
        return weekDataList;
    }
}