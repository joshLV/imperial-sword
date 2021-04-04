package cn.lovike.tool.basic.framework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

/**
 * @author lovike
 * @since 2020-02-09
 */
@Configuration
public class CorsConfig {
    private final static List<String> METHODS        = Arrays.asList("OPTIONS", "HEAD", "GET", "PUT", "POST", "DELETE", "PATCH");
    private final static List<String> ALLOWED_ORIGIN = Arrays.asList("http://localhost:7080", "http://localhost:7081");

    @Bean
    public CorsFilter corsFilter() {
        // 1.添加 CORS 配置信息
        CorsConfiguration config = new CorsConfiguration();
        // 1.1 注意允许的域不要写 * ，否则 cookie 将无法使用，可添加多个域
        for (String origin : ALLOWED_ORIGIN) {
            config.addAllowedOrigin(origin);
        }
        // 1.2 是否发送 cookie 信息
        config.setAllowCredentials(true);
        // 1.3 允许的请求方式
        for (String method : METHODS) {
            config.addAllowedMethod(method);
        }
        // 1.4 允许的头信息
        config.addAllowedHeader("*");
        // 1.5 有效时长
        config.setMaxAge(3600L);

        // 2. 添加映射路径，拦截一切请求
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", config);

        // 3. 返回新的 CorsFilter
        return new CorsFilter(configSource);
    }
}