# plant-helper-service

植物种植助手小程序后端服务，单机服务，不需要分布式，基于微信云托管 Spring Boot 框架实现。

## 项目信息

| 项目信息 | 说明 |
|---------|------|
| 项目名称 | plant-helper-service |
| 框架 | Spring Boot 2.7.18 + Java 8 |
| 数据库 | MySQL + MyBatis（注解方式） |
| 分页插件 | PageHelper |
| 部署平台 | 微信云托管 |
| 构建工具 | Maven |
| 容器化 | Docker |

## 快速开始

### 本地开发
1. **环境准备**：Java 8, Maven 3.6+, MySQL 8.0+
2. **数据库初始化**：执行 `docs/db.sql` 创建数据库和表
3. **配置文件**：复制 `application-dev.yml` 并根据本地环境修改数据库连接
4. **启动应用**：
   ```bash
   mvn clean spring-boot:run
   ```
5. **访问接口**：应用运行在 `http://localhost:18080`

### Docker运行
```bash
# 构建镜像
docker build -t plant-helper-service .

# 运行容器
docker run -p 18080:18080 \
  -e SPRING_PROFILES_ACTIVE=dev \
  -e WX_APPID=your_appid \
  -e WX_SECRET=your_secret \
  plant-helper-service
```

### 运行测试
```bash
# 运行所有测试
mvn test

# 运行特定测试类
mvn test -Dtest=AuthControllerTest

# 生成测试报告
mvn surefire-report:report
```

### Hutool工具使用示例
项目已集成Hutool工具库，以下为常用工具示例：

**JSON处理**（已替换fastjson）：
```java
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

// JSON字符串解析
JSONObject json = JSONUtil.parseObj("{\"name\":\"value\"}");
String name = json.getStr("name");

// 对象转JSON
String jsonStr = JSONUtil.toJsonStr(user);
```

**HTTP客户端**（已替换Apache HttpClient）：
```java
import cn.hutool.http.HttpUtil;

// GET请求
String response = HttpUtil.get("https://api.example.com/data");

// POST请求（带参数）
Map<String, Object> params = new HashMap<>();
params.put("key", "value");
String postResponse = HttpUtil.post("https://api.example.com/submit", params);
```

**常用工具**：
```java
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.date.DateUtil;

// 唯一ID生成（已用于Token生成）
String uuid = IdUtil.simpleUUID();

// 字符串工具
boolean isBlank = StrUtil.isBlank("");  // true
String trimmed = StrUtil.trim("  text  ");

// 日期工具
String now = DateUtil.now();
Date date = DateUtil.parse("2023-01-01");
```

## 功能模块

1. **用户认证 (Auth)** - 微信小程序登录、用户信息管理、Token认证
2. **植物管理 (Plant)** - 植物的CRUD操作、分页查询
3. **种植记录 (Record)** - 记录植物生长过程（浇水、施肥、生长、拍照）
4. **提醒管理 (Reminder)** - 植物养护提醒设置与调度

## 技术栈

- Spring Boot 2.7.18
- MyBatis（注解方式，无XML映射文件）
- MySQL 8.0+
- Lombok（简化代码）
- Validation（参数校验）
- PageHelper 1.4.3（分页插件）
- Hutool 5.8.25（Java工具库，包含JSON处理、HTTP客户端、加密、字符串处理等）

## 目录结构

```
.
├── Dockerfile                      Dockerfile 文件
├── LICENSE                         LICENSE 文件
├── README.md                       README 文件
├── container.config.json           模板部署「服务设置」初始化配置
├── mvnw                            mvnw 文件，处理maven版本兼容问题
├── mvnw.cmd                        mvnw.cmd 文件，处理maven版本兼容问题
├── pom.xml                         pom.xml文件
├── settings.xml                    maven 配置文件
└── src
    ├── main
    │   ├── java/com/tencent/wxcloudrun
    │   │   ├── WxCloudRunApplication.java       # 启动类
    │   │   ├── config/                           # 配置类
    │   │   │   └── WxConfig.java                # 微信小程序配置
    │   │   ├── context/                          # 上下文存储
    │   │   │   └── TokenContext.java            # Token内存存储（ConcurrentHashMap）
    │   │   ├── controller/                       # 控制器层
    │   │   │   ├── AuthController.java          # 认证接口
    │   │   │   ├── PlantController.java          # 植物管理接口
    │   │   │   ├── RecordController.java         # 种植记录接口
    │   │   │   └── ReminderController.java       # 提醒管理接口
    │   │   ├── service/                          # 服务层接口
    │   │   │   ├── AuthService.java
    │   │   │   ├── PlantService.java
    │   │   │   ├── RecordService.java
    │   │   │   └── ReminderService.java
    │   │   ├── service/impl/                     # 服务实现
    │   │   │   ├── AuthServiceImpl.java
    │   │   │   ├── PlantServiceImpl.java
    │   │   │   ├── RecordServiceImpl.java
    │   │   │   └── ReminderServiceImpl.java
    │   │   ├── dao/                              # 数据访问层（MyBatis注解方式）
    │   │   │   ├── UserMapper.java
    │   │   │   ├── PlantMapper.java
    │   │   │   ├── RecordMapper.java
    │   │   │   └── ReminderMapper.java
    │   │   ├── model/                            # 实体类
    │   │   │   ├── User.java
    │   │   │   ├── Plant.java
    │   │   │   ├── Record.java
    │   │   │   └── Reminder.java
    │   │   ├── dto/                              # 数据传输对象
    │   │   │   ├── req/                          # 请求DTO
    │   │   │   │   ├── LoginRequest.java
    │   │   │   │   ├── ProfileRequest.java
    │   │   │   │   ├── PageQueryRequest.java
    │   │   │   │   ├── RecordPageQueryRequest.java
    │   │   │   │   ├── ReminderPageQueryRequest.java
    │   │   │   │   ├── PlantRequest.java
    │   │   │   │   ├── RecordRequest.java
    │   │   │   │   └── ReminderRequest.java
    │   │   │   └── resp/                         # 响应DTO
    │   │   │       ├── ApiResponse.java
    │   │   │       ├── LoginResponse.java
    │   │   │       ├── ProfileResponse.java
    │   │   │       └── PageResponse.java
    │   │   ├── exception/                        # 异常处理
    │   │   │   ├── BusinessException.java
    │   │   │   └── GlobalExceptionHandler.java
    │   │   └── constant/                         # 枚举常量
    │   │       └── CodeEnum.java
    │   └── resources                             # 资源文件
    │       ├── application.yml                    # 应用配置
    │       ├── application-dev.yml               # 开发环境配置
    │       ├── application-prod.yml              # 生产环境配置
    │       └── db.sql                            # 数据库初始化脚本
    └── test                                      # 测试代码
        └── java/com/tencent/wxcloudrun
            ├── controller/                        # 控制器测试
            │   ├── AuthControllerTest.java
            │   └── PlantControllerTest.java
            └── service/impl/                      # 服务测试
                ├── AuthServiceImplTest.java
                ├── PlantServiceImplTest.java
                ├── RecordServiceImplTest.java
                └── ReminderServiceImplTest.java
```

## 认证模块

### 登录流程

```
小程序端                          开发者服务器                    微信接口服务
  │                                  │                              │
  │── wx.login() 获取 code          │                              │
  │─────────────────────────────────>│                              │
  │                                  │── code + appid + secret     │
  │                                  │─────────────────────────────>│
  │                                  │<── session_key + openid ────│
  │                                  │                              │
  │                                  │── 生成自定义登录态 (token)   │
  │                                  │── 返回 token + userId       │
  │<─────────────────────────────────│                              │
```

### 接口列表

| 接口 | 方法 | 路径 | 说明 |
|-----|------|------|------|
| 登录 | POST | `/api/auth/login` | 微信登录，获取token |
| 获取用户信息 | POST | `/api/auth/getProfile` | 获取当前用户信息（需Token） |
| 更新用户信息 | POST | `/api/auth/updateProfile` | 更新昵称头像（需Token） |
| 退出登录 | POST | `/api/auth/logout` | 清除登录态（需Token） |

**注意**：当前实现与文档描述存在差异，手机号获取功能尚未实现。

### 请求示例

**登录**
```json
POST /api/auth/login
{
  "code": "微信wx.login返回的code"
}
```

**响应**
```json
{
  "code": 0,
  "data": {
    "token": "自定义登录态token",
    "userId": "用户openid",
    "isNewUser": true
  }
}
```

### Token 认证

后续请求需要在请求头中携带 Token：
```
X-Token: your_token_here
```

### Token 存储机制

- **存储方式**：内存存储（ConcurrentHashMap），适合单机部署
- **有效期**：30天（30 * 24 * 60 * 60 * 1000毫秒）
- **数据结构**：
  - Token缓存：token → TokenInfo（openid, userId, expireTime）
  - OpenID缓存：openid → token（用于快速查找）
- **注意事项**：
   1. **微信API URL错误**：已修复，使用正确的 `jscode2session` API
  2. **分布式部署限制**：内存存储（ConcurrentHashMap）不适合生产分布式部署（需替换为Redis）
  3. **认证验证重复**：Token验证在每个控制器方法中手动调用，代码重复
  4. **缺少统一拦截器**：无统一认证拦截器，维护困难

## 分页查询

所有列表查询接口统一使用 POST + 请求体：

| 接口 | 方法 | 路径 | 请求DTO |
|-----|------|------|---------|
| 植物列表 | POST | `/api/plants/page` | PageQueryRequest |
| 记录列表 | POST | `/api/records/page` | RecordPageQueryRequest |
| 提醒列表 | POST | `/api/reminders/page` | ReminderPageQueryRequest |

**植物列表请求示例**
```json
POST /api/plants/page
{
  "pageNum": 1,
  "pageSize": 10,
  "userId": "用户openid"
}
```

**分页响应格式**
```json
{
  "code": 0,
  "data": {
    "list": [...],          // 数据列表
    "total": 100,           // 总记录数
    "pageNum": 1,           // 当前页码
    "pageSize": 10,         // 每页大小
    "totalPages": 10        // 总页数
  }
}
```

### 认证方式注意

**当前存在认证方式不一致问题**：
1. **新认证模块**：使用 `X-Token` 请求头进行Token验证
2. **原有控制器**：使用 `X-User-ID` 请求头直接传递用户ID
3. **建议统一**：所有接口使用 `X-Token` + 统一认证拦截器

## 本地调试

下载代码在本地调试，请参考[微信云托管本地调试指南](https://developers.weixin.qq.com/miniprogram/dev/wxcloudrun/src/guide/debug/)。

## 实时开发

代码变动时，不需要重新构建和启动容器，即可查看变动后的效果。请参考[微信云托管实时开发指南](https://developers.weixin.qq.com/miniprogram/dev/wxcloudrun/src/guide/debug/dev.html)

## Dockerfile最佳实践

请参考[如何提高项目构建效率](https://developers.weixin.qq.com/miniprogram/dev/wxcloudrun/src/scene/build/speed.html)

## 环境变量

部署时需要在「服务设置」中配置以下环境变量：

- **数据库配置**：
  - MYSQL_ADDRESS - MySQL地址（如：localhost:3306）
  - MYSQL_USERNAME - MySQL用户名
  - MYSQL_PASSWORD - MySQL密码
  - MYSQL_DATABASE - 数据库名称（默认为plant_helper）

- **应用配置**：
  - SPRING_PROFILES_ACTIVE - 运行环境（dev/test/prod）

- **微信小程序配置**：
  - WX_APPID - 微信小程序AppID
  - WX_SECRET - 微信小程序AppSecret

### 数据库说明
项目使用以下数据库表：
1. **users** - 用户信息表（openid, nickname, avatar_url）
2. **plants** - 植物信息表（名称、类型、状态、浇水施肥间隔）
3. **records** - 生长记录表（浇水、施肥、生长、拍照记录）
4. **reminders** - 提醒表（浇水、施肥、自定义提醒）

完整SQL脚本见：[docs/db.sql](./docs/db.sql)

## 已识别问题与改进建议

### 高优先级问题（影响功能）
1. **微信API URL错误**：**已修复**，使用正确的 `jscode2session` API
2. **认证方式不一致**：新旧控制器分别使用 `X-Token` 和 `X-User-ID` 请求头
3. **缺少统一认证拦截器**：每个控制器方法需手动验证Token，代码重复

### 中优先级问题（安全与架构）
1. **Token存储限制**：内存存储（ConcurrentHashMap）不适合生产分布式部署
2. **缺少API文档**：无Swagger/OpenAPI文档，接口调试困难
3. **缺少性能监控**：无健康检查、指标监控和日志聚合

### 低优先级问题（优化与维护）
1. **测试覆盖率**：单元测试可进一步完善，缺少集成测试
2. **配置管理**：敏感配置硬编码，缺乏配置中心集成
3. **代码规范**：部分代码缺少注释，命名可进一步优化

## 改进路线图

### 阶段一：关键修复（立即执行）
1. **添加统一认证拦截器**：实现 `HandlerInterceptor` 自动验证Token
2. **统一认证方式**：所有接口改用 `X-Token` 请求头

### 阶段二：安全加固（1-2周）
1. **添加Swagger文档**：自动生成API文档，支持在线测试

### 阶段三：架构优化（2-4周）
1. **完善异常处理**：扩展 `GlobalExceptionHandler`，添加业务异常
2. **优化数据库连接**：配置连接池参数，添加慢SQL监控


## License

[MIT](./LICENSE)
