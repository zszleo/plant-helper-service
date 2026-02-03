-- 植物种植助手数据库初始化脚本

-- 创建用户表（用于数据关联，无需认证）
CREATE TABLE IF NOT EXISTS `users` (
                                       `id` int(11) NOT NULL AUTO_INCREMENT,
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
                                        `id` int(11) NOT NULL AUTO_INCREMENT,
    `user_id` varchar(64) NOT NULL COMMENT '用户ID',
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
                                         `id` int(11) NOT NULL AUTO_INCREMENT,
    `user_id` varchar(64) NOT NULL COMMENT '用户ID',
    `plant_id` int(11) NOT NULL COMMENT '关联植物ID',
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
                                           `id` int(11) NOT NULL AUTO_INCREMENT,
    `user_id` varchar(64) NOT NULL COMMENT '用户ID',
    `plant_id` int(11) NOT NULL COMMENT '关联植物ID',
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

-- 插入测试数据（可选）
INSERT IGNORE INTO `users` (`id`, `openid`, `nickname`, `avatar_url`) VALUES 
(1, 'test_user_001', '测试用户', 'https://example.com/avatar.jpg');

INSERT IGNORE INTO `plants` (`id`, `user_id`, `name`, `type`, `description`, `plant_date`, `status`) VALUES 
(1, 'test_user_001', '多肉植物', 'succulent', '耐旱植物，适合室内养殖', NOW(), 'healthy');

INSERT IGNORE INTO `records` (`id`, `user_id`, `plant_id`, `type`, `record_time`, `notes`) VALUES 
(1, 'test_user_001', 1, 'watering', NOW(), '第一次浇水');

INSERT IGNORE INTO `reminders` (`id`, `user_id`, `plant_id`, `type`, `time`, `frequency`, `frequency_type`, `next_remind_time`) VALUES 
(1, 'test_user_001', 1, 'watering', '09:00', 7, 'weekly', DATE_ADD(NOW(), INTERVAL 7 DAY));