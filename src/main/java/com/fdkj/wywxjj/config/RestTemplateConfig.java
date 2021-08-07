package com.fdkj.wywxjj.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * 接口调用配置类
 *
 * @author wyt
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(getFactory());
    }

    private ClientHttpRequestFactory getFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        //连接超时时间
        factory.setConnectTimeout(60 * 1000);
        //读取超时时间
        factory.setReadTimeout(60 * 1000);
        return factory;
    }
}
