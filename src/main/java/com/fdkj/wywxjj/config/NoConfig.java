package com.fdkj.wywxjj.config;

import com.fdkj.wywxjj.utils.NoUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 订单编号生成配置
 *
 * @author wyt
 */
@Configuration
public class NoConfig {

    /**
     * 机房id
     */
    @Value("${orderNo.datacenterId}")
    private long datacenterId;

    /**
     * 机器id
     */
    @Value("${orderNo.machineId}")
    private long machineId;

    @Bean
    public NoUtil orderNoUtil() {
        return new NoUtil(datacenterId, machineId);
    }

}
