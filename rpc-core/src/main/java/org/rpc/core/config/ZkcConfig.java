package org.rpc.core.config;


import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Component
public class ZkcConfig {
    @Bean
    public CuratorFramework getClient(){
        return CuratorFrameworkFactory.builder()
                .connectString("182.92.192.154:2181")
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .connectionTimeoutMs(15 * 1000) //连接超时时间，默认15秒
                .sessionTimeoutMs(60 * 1000) //会话超时时间，默认60秒
                .namespace("simple-rpc") //设置命名空间
                .build();
    }

}
