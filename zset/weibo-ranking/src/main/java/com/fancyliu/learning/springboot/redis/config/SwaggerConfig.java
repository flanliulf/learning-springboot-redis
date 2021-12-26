package com.fancyliu.learning.springboot.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Swagger 配置类 (3.0 版本)
 *
 * @author : Fancy Liu
 * @since : 2021/12/26 10:10 下午
 */
@Configuration
@EnableOpenApi
public class SwaggerConfig {

	@Bean
	public Docket docket() {
		return new Docket(DocumentationType.OAS_30)
				.apiInfo(apiInfo()).enable(true)
				.select()
				//apis： 添加swagger接口提取范围
				.apis(RequestHandlerSelectors.basePackage("com.fancyliu"))
				//.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
				.paths(PathSelectors.any())
				.build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("微博热搜排行榜项目接口文档")
				.description("微博热搜排行榜项目描述")
				.contact(new Contact("fancyliu", "www.fancyliu.com", "flanliulf@gmail.com"))
				.version("1.0")
				.build();
	}
}
