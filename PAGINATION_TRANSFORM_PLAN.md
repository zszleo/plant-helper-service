# 分页查询改造方案

## 一、改造目标

将所有列表查询接口改造为分页查询，统一使用 POST 方法。

## 二、改造接口清单

| 模块 | 原接口 | 改造后接口 |
|-----|--------|-----------|
| 植物 | `GET /api/plants` | `POST /api/plants/page` |
| 记录 | `GET /api/records` | `POST /api/records/page` |
| 记录 | `GET /api/records/plant/{plantId}` | `POST /api/records/page` |
| 提醒 | `GET /api/reminders` | `POST /api/reminders/page` |
| 提醒 | `GET /api/reminders/plant/{plantId}` | `POST /api/reminders/page` |

## 三、技术方案

- **分页插件**：PageHelper
- **排序规则**：统一 `ORDER BY create_time DESC`
- **userId**：从请求体中获取，不再使用请求头

## 四、新增文件

| 文件 | 路径 | 说明 |
|-----|------|------|
| `PageQueryRequest.java` | `dto/req/` | 通用分页查询请求 |
| `RecordPageQueryRequest.java` | `dto/req/` | 记录分页查询请求 |
| `ReminderPageQueryRequest.java` | `dto/req/` | 提醒分页查询请求 |
| `PageResponse.java` | `dto/resp/` | 分页响应数据 |

## 五、修改文件

| 文件 | 改动内容 |
|-----|---------|
| `pom.xml` | 添加 PageHelper 依赖 |
| `dto/resp/ApiResponse.java` | 添加 `pageOk()` 方法 |
| `controller/PlantController.java` | `getPlants` → `POST /page` |
| `controller/RecordController.java` | 改造为分页查询 |
| `controller/ReminderController.java` | 改造为分页查询 |
| `service/PlantService.java` | 返回 `PageResponse<Plant>` |
| `service/impl/PlantServiceImpl.java` | 使用 PageHelper |
| `service/RecordService.java` | 返回 `PageResponse<Record>` |
| `service/impl/RecordServiceImpl.java` | 使用 PageHelper |
| `service/ReminderService.java` | 返回 `PageResponse<Reminder>` |
| `service/impl/ReminderServiceImpl.java` | 使用 PageHelper |
| `dao/RecordMapper.java` | 添加 `findByPlantId` |
| `dao/ReminderMapper.java` | 添加 `findByPlantId` |

## 六、请求/响应结构

### 6.1 请求体

```json
// PageQueryRequest（通用）
{
  "pageNum": 1,        // 页码，默认1
  "pageSize": 10,      // 每页条数，默认10
  "userId": "user123"  // 用户ID，必填
}

// RecordPageQueryRequest
{
  "pageNum": 1,
  "pageSize": 10,
  "userId": "user123",
  "plantId": 123       // 可选，植物ID筛选
}

// ReminderPageQueryRequest
{
  "pageNum": 1,
  "pageSize": 10,
  "userId": "user123",
  "plantId": 123       // 可选，植物ID筛选
}
```

### 6.2 响应体

```json
{
  "code": 0,
  "message": "",
  "data": {
    "list": [...],
    "total": 100,
    "pageNum": 1,
    "pageSize": 10,
    "totalPages": 10
  }
}
```

## 七、代码示例

### 7.1 PageQueryRequest.java

```java
package com.tencent.wxcloudrun.dto.req;

import lombok.Data;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class PageQueryRequest {

    @Min(value = 1, message = "页码必须大于0")
    private Integer pageNum = 1;

    @Min(value = 1, message = "每页条数必须大于0")
    private Integer pageSize = 10;

    @NotBlank(message = "userId不能为空")
    private String userId;
}
```

### 7.2 PageResponse.java

```java
package com.tencent.wxcloudrun.dto.resp;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class PageResponse<T> {
    private List<T> list;
    private Long total;
    private Integer pageNum;
    private Integer pageSize;
    private Integer totalPages;
}
```

### 7.3 ApiResponse.java 新增方法

```java
public static <T> ApiResponse<PageResponse<T>> pageOk(PageResponse<T> data) {
    return new ApiResponse<>(CodeEnum.SUCCESS.getCode(), "", data);
}
```

### 7.4 Controller 示例

```java
@PostMapping("/page")
public ApiResponse<PageResponse<Plant>> getPlantsPage(@RequestBody PageQueryRequest request) {
    PageResponse<Plant> pageData = plantService.getPlantsByUserId(request);
    return ApiResponse.pageOk(pageData);
}
```

### 7.5 Service 示例

```java
@Override
public PageResponse<Plant> getPlantsByUserId(PageQueryRequest request) {
    PageHelper.startPage(request.getPageNum(), request.getPageSize());
    List<Plant> list = plantMapper.findByUserId(request.getUserId());
    Page<Plant> page = (Page<Plant>) list;

    return PageResponse.<Plant>builder()
            .list(page.getResult())
            .total(page.getTotal())
            .pageNum(request.getPageNum())
            .pageSize(request.getPageSize())
            .totalPages(page.getPages())
            .build();
}
```

## 八、改造顺序

1. 添加 PageHelper 依赖
2. 创建 DTO 文件
3. 修改 ApiResponse 添加 helper 方法
4. 改造 Controller 层
5. 改造 Service 层
6. 改造 Mapper 层
7. 测试验证

## 九、注意事项

- 原有 GET 接口直接废弃，不再保留
- 所有分页查询统一使用 POST + 请求体
- userId 从请求体中获取，不再使用请求头
- 排序规则固定为 create_time DESC
