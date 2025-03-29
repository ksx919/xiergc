package com.example.xiergc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStaticMasterReplicaConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import io.lettuce.core.ReadFrom;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.master.host}")
    private String masterHost;

    @Value("${spring.data.redis.master.port}")
    private int masterPort;

    @Value("${spring.data.redis.slave.host}")
    private String replicaHost;

    @Value("${spring.data.redis.slave.port}")
    private int replicaPort;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        // 1. 定义主节点（写操作）
        RedisStaticMasterReplicaConfiguration staticConfig =
                new RedisStaticMasterReplicaConfiguration(masterHost, masterPort);

        // 2. 添加从节点（读操作）
        staticConfig.addNode(replicaHost, replicaPort);

        // 3. 配置读写分离策略（优先从从节点读取）
        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .readFrom(ReadFrom.REPLICA_PREFERRED)
                .build();

        return new LettuceConnectionFactory(staticConfig, clientConfig);
    }
}
