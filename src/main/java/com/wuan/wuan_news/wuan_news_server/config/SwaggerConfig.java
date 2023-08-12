package com.wuan.wuan_news.wuan_news_server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/08/06/ 15:08
 * @description
 */
@EnableOpenApi
@Configuration
public class SwaggerConfig {
    /**
     * Created with IntelliJ IDEA.
     *
     * @author Jingzhen
     * @date 2023/8/12 18:23
     * @description Swagger 文档的基本信息
     * @return: springfox.documentation.service.ApiInfo
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Swagger 接口文档")
                .description("Swagger 接口文档")
                .version("3.0")
                .build();
    }

    @Bean
    public Docket apiWithAuthorization() {
        return new Docket(DocumentationType.OAS_30)
                // 为此Docket设置一个组名
                .groupName("withAuthorization")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.wuan.wuan_news.wuan_news_server.controller"))
                // 排除登录和注册路径
                .paths(input -> !"/wuan_news/api/authentication/login".equals(input) &&
                        !"/wuan_news/api/authentication/register".equals(input))
                .build()
                // 设置安全方案
                .securitySchemes(Collections.singletonList(apiKey()))
                // 设置安全上下文
                .securityContexts(Collections.singletonList(securityContext()));
    }

    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                // 这里是使用token的路径配置
                .operationSelector(operationContext -> {
                    String path = operationContext.requestMappingPattern();
                    return !"/wuan_news/api/authentication/login".equals(path) &&
                            !"/wuan_news/api/authentication/register".equals(path);
                })
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(new SecurityReference("Authorization", authorizationScopes));
    }

    @Bean
    public Docket apiWithoutAuthorization() {
        return new Docket(DocumentationType.OAS_30)
                // 为此Docket设置一个组名
                .groupName("withoutAuthorization")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.wuan.wuan_news.wuan_news_server.controller"))
                // 仅选择登录和注册路径
                .paths(input -> "/wuan_news/api/authentication/login".equals(input) ||
                        "/wuan_news/api/authentication/register".equals(input))
                .build();
    }
}
