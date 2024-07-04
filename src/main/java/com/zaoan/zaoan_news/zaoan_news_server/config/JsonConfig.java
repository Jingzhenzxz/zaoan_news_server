package com.zaoan.zaoan_news.zaoan_news_server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * Spring MVC Json 配置
 * @author Jingzhen
 */
@JsonComponent
public class JsonConfig {

    /**
     * 配置 Jackson 的 ObjectMapper 来解决 Long 类型数据在序列化成 JSON 时的精度丢失问题。
     * 通过自定义序列化器，将 Long 类型数据转换为 String 类型，从而在 JavaScript 中可以准确处理。
     *
     * @param builder 由 Spring Boot 自动配置提供的 Jackson2ObjectMapperBuilder，用于构建和配置 ObjectMapper
     * @return 配置后的 ObjectMapper
     */
    @Bean
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        // 创建 ObjectMapper 实例，禁用 XML 映射器（大多数 JSON 应用不需要处理 XML）
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();

        // 创建 SimpleModule 来添加自定义序列化器
        SimpleModule module = new SimpleModule();

        // 为 Long 类型添加自定义序列化器，使用 ToStringSerializer 将 Long 转换为 String
        // 这防止了大数值在 JavaScript 中的精度丢失
        module.addSerializer(Long.class, ToStringSerializer.instance);

        // 也为 Long 的基本类型（long）添加同样的序列化器
        module.addSerializer(Long.TYPE, ToStringSerializer.instance);

        // 将模块注册到 ObjectMapper 中
        objectMapper.registerModule(module);

        // 返回配置好的 ObjectMapper 实例
        return objectMapper;
    }
}