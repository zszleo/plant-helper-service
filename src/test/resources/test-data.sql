-- 测试用户数据
INSERT INTO users (id, openid, nickname) VALUES 
(1, 'test_user_001', '测试用户');

-- 测试植物数据
INSERT INTO plants (id, user_id, name, type, plant_date, status) VALUES 
(1, 'test_user_001', '多肉植物', 'succulent', NOW(), 'healthy'),
(2, 'test_user_001', '绿萝', 'ivy', NOW(), 'growing');

-- 测试记录数据
INSERT INTO records (id, user_id, plant_id, type, record_time, notes) VALUES 
(1, 'test_user_001', 1, 'watering', NOW(), '第一次浇水');

-- 测试提醒数据
INSERT INTO reminders (id, user_id, plant_id, type, time, frequency, frequency_type, next_remind_time, is_enabled) VALUES 
(1, 'test_user_001', 1, 'watering', '09:00', 7, 'weekly', DATE_ADD(NOW(), INTERVAL 7 DAY), 1);