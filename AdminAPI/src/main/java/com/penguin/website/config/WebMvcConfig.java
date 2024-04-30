package com.penguin.website.config;

import javax.servlet.MultipartConfigElement;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import com.penguin.website.web.interceptor.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
@ConditionalOnExpression("${swagger.enable:true}")
public class WebMvcConfig implements WebMvcConfigurer {
	@Autowired
	private LoginInterceptor loginAuthenticator;
	@Autowired
	private IndexInterceptor indexInterceptor;
	@Autowired
	private InstallInterceptor installInterceptor;
	/*@Autowired
	private PenguinInterceptor penguinInterceptor;*/

	@Autowired
	private SystemRequestTokenIntercept systemRequestTokenIntercept;

	@Value("${imageUrlYS}")
	private String imageUrlYS;

	@Value("${imageUrl}")
	private String imageUrl;

	/**
	 * 注册拦截器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(loginAuthenticator).addPathPatterns("/admin/**").excludePathPatterns("/admin/login")
				.excludePathPatterns("/admin/getLogin");
		registry.addInterceptor(indexInterceptor);
		registry.addInterceptor(systemRequestTokenIntercept);
		//registry.addInterceptor(penguinInterceptor);
		registry.addInterceptor(installInterceptor).addPathPatterns("/**").excludePathPatterns("/install")
				.excludePathPatterns("/install/execute").excludePathPatterns("/js/**").excludePathPatterns("/css/**")
				.excludePathPatterns("/img/**").excludePathPatterns("/plugins/**");
	}

	/**
	 * 释放静态资源
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**")
				.addResourceLocations("classpath:/static/");
		// 通过addResourceHandler添加资源映射路径，然后通过addResourceLocations来指定路径。可以访问自定义upload文件夹
//		registry.addResourceHandler("/upload/**")
//				.addResourceLocations("classpath:/upload/")
//				.addResourceLocations("file:///" + System.getProperties().getProperty("user.home") + "/penguin/upload/");
		registry.addResourceHandler("/upload/**")
				.addResourceLocations("file:" + imageUrl+"upload/");
		registry.addResourceHandler("/website/**")
				.addResourceLocations("file:" + imageUrlYS);
		registry.addResourceHandler("/source/**")
				.addResourceLocations("classpath:/templates/themes/");
		registry.addResourceHandler("/swagger-ui.html")
				.addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/");
		registry.addResourceHandler("/home/**")
				.addResourceLocations("classpath:/");
		WebMvcConfigurer.super.addResourceHandlers(registry);
	}

	/**
	 * 文件上传配置
	 *
	 * @return
	 */
	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		// 单个文件最大 KB,MB
		factory.setMaxFileSize("102400KB");
		/// 设置总上传数据总大小
		factory.setMaxRequestSize("102400KB");
		return factory.createMultipartConfig();
	}

	@Bean
	public Docket createDocket(){
		Docket docket = new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(new ApiInfoBuilder().title("接口API")
						.description("相关接口API文档")
						.version("1.1").build())
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.penguin.website.web.controller"))
				.paths(PathSelectors.any())
				.build();
		return docket;
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("Swagger2")
				.description("RESTful API接口")
				.version("1.0.1")
				.build();
	}
}
