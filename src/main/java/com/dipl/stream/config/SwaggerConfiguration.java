package com.dipl.stream.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Profile({"!prod && swagger"})
@EnableSwagger2
@Configuration
public class SwaggerConfiguration {

	@Bean
	public Docket produceApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.dipl.stream.controller"))
				.paths(PathSelectors.any()).build().apiInfo(apiInfo());
	}
	// Describe your apis
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("livestream").description("livestream service")
				.version("1.0").build();
	}


}
