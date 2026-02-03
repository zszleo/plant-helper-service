# plant-helper-service

植物种植助手小程序后端服务，基于微信云托管 Spring Boot 框架实现。

## 项目信息

| 项目信息 | 说明 |
|---------|------|
| 项目名称 | plant-helper-service |
| 框架 | Spring Boot 2.6.6 + Java 8 |
| 数据库 | MySQL + MyBatis |
| 部署平台 | 微信云托管 |

## 功能模块

1. **植物管理 (Plant)** - 植物的CRUD操作
2. **种植记录 (Record)** - 记录植物生长过程
3. **提醒管理 (Reminder)** - 植物养护提醒

## 技术栈

- Spring Boot 2.6.6
- MyBatis
- MySQL
- Lombok
- Validation

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
    │   │   ├── controller/                       # 控制器层
    │   │   │   ├── PlantController.java         # 植物管理接口
    │   │   │   ├── RecordController.java        # 种植记录接口
    │   │   │   └── ReminderController.java      # 提醒管理接口
    │   │   ├── service/                          # 服务层
    │   │   │   ├── PlantService.java
    │   │   │   ├── RecordService.java
    │   │   │   └── impl/                         # 服务实现
    │   │   ├── dao/                              # 数据访问层
    │   │   │   ├── PlantMapper.java
    │   │   │   ├── RecordMapper.java
    │   │   │   └── ReminderMapper.java
    │   │   ├── model/                            # 实体类
    │   │   │   ├── Plant.java
    │   │   │   ├── Record.java
    │   │   │   └── Reminder.java
    │   │   ├── dto/                              # 数据传输对象
    │   │   │   ├── req/                          # 请求DTO
    │   │   │   └── resp/ApiResponse.java         # 统一响应封装
    │   │   ├── exception/                        # 异常处理
    │   │   │   ├── BusinessException.java
    │   │   │   └── GlobalExceptionHandler.java
    │   │   └── constant/CodeEnum.java            # 枚举常量
    │   └── resources                              # 资源文件
    └── test                                       # 测试代码
```

## 本地调试

下载代码在本地调试，请参考[微信云托管本地调试指南](https://developers.weixin.qq.com/miniprogram/dev/wxcloudrun/src/guide/debug/)。

## 实时开发

代码变动时，不需要重新构建和启动容器，即可查看变动后的效果。请参考[微信云托管实时开发指南](https://developers.weixin.qq.com/miniprogram/dev/wxcloudrun/src/guide/debug/dev.html)

## Dockerfile最佳实践

请参考[如何提高项目构建效率](https://developers.weixin.qq.com/miniprogram/dev/wxcloudrun/src/scene/build/speed.html)

## 环境变量

部署时需要在「服务设置」中配置以下环境变量：

- MYSQL_ADDRESS - MySQL 地址
- MYSQL_PASSWORD - MySQL 密码
- MYSQL_USERNAME - MySQL 用户名
- SPRING_PROFILES_ACTIVE - 代码运行环境变量

## License

[MIT](./LICENSE)
