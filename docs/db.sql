-- 植物种植助手数据库初始化脚本

-- 删除现有表（如果存在）
DROP TABLE IF EXISTS `reminders`;
DROP TABLE IF EXISTS `records`;
DROP TABLE IF EXISTS `plants`;
DROP TABLE IF EXISTS `users`;
DROP TABLE IF EXISTS `wx_mini_program_log`;

-- 创建用户表（用于数据关联，无需认证）
CREATE TABLE IF NOT EXISTS `users` (
    `id` bigint NOT NULL,
    `openid` varchar(64) DEFAULT NULL COMMENT '微信openid',
    `nickname` varchar(100) DEFAULT NULL COMMENT '用户昵称',
    `avatar_url` varchar(500) DEFAULT NULL COMMENT '头像URL',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_openid` (`openid`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';

-- 创建植物表
CREATE TABLE IF NOT EXISTS `plants` (
    `id` bigint NOT NULL,
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `name` varchar(100) NOT NULL COMMENT '植物名称',
    `type` varchar(50) NOT NULL COMMENT '植物类型',
    `description` text COMMENT '描述信息',
    `image_url` varchar(500) DEFAULT NULL COMMENT '图片URL',
    `plant_date` datetime NOT NULL COMMENT '种植日期',
    `status` varchar(20) NOT NULL DEFAULT 'healthy' COMMENT '状态：healthy/growing/need-care/diseased',
    `watering_interval` int(11) DEFAULT 7 COMMENT '浇水间隔（天）',
    `fertilizing_interval` int(11) DEFAULT 30 COMMENT '施肥间隔（天）',
    `last_watering` datetime DEFAULT NULL COMMENT '上次浇水时间',
    `last_fertilizing` datetime DEFAULT NULL COMMENT '上次施肥时间',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='植物信息表';

-- 创建生长记录表
CREATE TABLE IF NOT EXISTS `records` (
    `id` bigint NOT NULL,
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `plant_id` bigint NOT NULL COMMENT '关联植物ID',
    `type` varchar(20) NOT NULL COMMENT '记录类型：watering/fertilizing/growth/photo',
    `record_time` datetime NOT NULL COMMENT '记录时间',
    `notes` text COMMENT '备注信息',
    `image_url` varchar(500) DEFAULT NULL COMMENT '图片URL',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_plant_id` (`plant_id`),
    KEY `idx_type` (`type`),
    KEY `idx_record_time` (`record_time`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='生长记录表';

-- 创建提醒表
CREATE TABLE IF NOT EXISTS `reminders` (
    `id` bigint NOT NULL,
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `plant_id` bigint NOT NULL COMMENT '关联植物ID',
    `type` varchar(20) NOT NULL COMMENT '提醒类型：watering/fertilizing/custom',
    `custom_type` varchar(50) DEFAULT NULL COMMENT '自定义类型名称',
    `time` varchar(10) NOT NULL COMMENT '提醒时间（HH:mm格式）',
    `frequency` int(11) NOT NULL COMMENT '频率（天数）',
    `frequency_type` varchar(20) NOT NULL COMMENT '频率类型：daily/weekly/monthly/custom',
    `next_remind_time` datetime NOT NULL COMMENT '下次提醒时间',
    `is_enabled` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_plant_id` (`plant_id`),
    KEY `idx_is_enabled` (`is_enabled`),
    KEY `idx_next_remind_time` (`next_remind_time`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='提醒信息表';

-- 小程序日志表
CREATE TABLE IF NOT EXISTS `wx_mini_program_log` (
    `id` bigint NOT NULL COMMENT '主键ID',
    `code` varchar(50) DEFAULT NULL COMMENT '错误码',
    `level` varchar(20) DEFAULT NULL COMMENT '日志级别',
    `context` varchar(255) DEFAULT NULL COMMENT '上下文信息',
    `api_name` varchar(255) DEFAULT NULL COMMENT '接口名称',
    `operation` varchar(100) DEFAULT NULL COMMENT '操作',
    `params` text COMMENT '请求参数（JSON格式）',
    `path` varchar(500) DEFAULT NULL COMMENT '路径',
    `device_info` json DEFAULT NULL COMMENT '设备信息（JSON格式）',
    `message` text COMMENT '日志消息',
    `processing_result` json DEFAULT NULL COMMENT '处理结果（JSON格式）',
    `source` varchar(100) DEFAULT NULL COMMENT '来源',
    `log_timestamp` timestamp DEFAULT NULL COMMENT '时间戳',
    `type` varchar(50) DEFAULT NULL COMMENT '类型',
    `user_message` text COMMENT '用户消息',
    `create_time` timestamp DEFAULT NULL COMMENT '创建时间',
    `update_time` timestamp DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='小程序日志表';