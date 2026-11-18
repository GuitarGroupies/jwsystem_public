package com.students.jwsystem_public.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        // 单机模式，填入你的 Redis IP、端口和密码
        config.useSingleServer()
                .setAddress("redis://192.168.180.100:6379") // ✅ 确认 IP 没变
                .setPassword("123456");                    // ⚠️ 换成你的密码
        return Redisson.create(config);
    }
}