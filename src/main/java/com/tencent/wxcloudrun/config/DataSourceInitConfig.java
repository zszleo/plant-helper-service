package com.tencent.wxcloudrun.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据库连接初始化配置
 * 在应用启动时初始化数据库连接，确保连接池正常工作
 * @author zszleon
 */
@Slf4j
@Configuration
public class DataSourceInitConfig implements ApplicationListener<ApplicationReadyEvent> {

    @Resource
    private DataSource dataSource;
    @Value("${spring.profiles.active:dev}")
    private String env;
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("开始初始化数据库连接...");
        
        try {
            HikariDataSource hikariDataSource = (HikariDataSource) dataSource;
            if (!env.equals("prod")) {
                // 打印连接池配置信息
                log.info("HikariCP连接池配置:");
                log.info("  - 连接池名称: {}", hikariDataSource.getPoolName());
                log.info("  - 最小空闲连接数: {}", hikariDataSource.getMinimumIdle());
                log.info("  - 最大连接数: {}", hikariDataSource.getMaximumPoolSize());
                log.info("  - 连接超时时间: {}ms", hikariDataSource.getConnectionTimeout());
                log.info("  - 空闲连接超时时间: {}ms", hikariDataSource.getIdleTimeout());
                log.info("  - 连接最大存活时间: {}ms", hikariDataSource.getMaxLifetime());
                log.info("  - JDBC URL: {}", hikariDataSource.getJdbcUrl());
                log.info("  - 用户名: {}", hikariDataSource.getUsername());
            }

            // 预热连接池
            warmUpConnectionPool(hikariDataSource);

            log.info("数据库连接初始化完成！");
        } catch (Exception e) {
            log.error("数据库连接初始化失败", e);
            throw new RuntimeException("数据库连接初始化失败", e);
        }
    }

    /**
     * 预热连接池
     * 创建最小数量的连接，确保应用启动后能快速响应请求
     */
    private void warmUpConnectionPool(HikariDataSource hikariDataSource) {
        int minimumIdle = hikariDataSource.getMinimumIdle();
        log.info("开始预热连接池，将创建 {} 个连接...", minimumIdle);
        
        try {
            for (int i = 0; i < minimumIdle; i++) {
                try (Connection connection = hikariDataSource.getConnection()) {
                    // 执行简单查询验证连接
                    connection.createStatement().execute("SELECT 1");
                }
            }
            log.info("连接池预热完成，已创建 {} 个连接", minimumIdle);
        } catch (SQLException e) {
            log.error("连接池预热失败", e);
            throw new RuntimeException("连接池预热失败", e);
        }
    }
}
