package io.quacker.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

@Configuration
public class WebConfig {

    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer customize() {
        return p -> {
            p.setOneIndexedParameters(true);  // page=1부터 시작
            p.setMaxPageSize(50);             // 최대 size 제한
            p.setFallbackPageable(PageRequest.of(0, 10)); // 기본 page=0, size=10
        };
    }
}
