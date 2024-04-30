package com.penguin.website.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.penguin.website.config.thymeleaf.dialect.ThSysDialect;

@Configuration
public class ThymeleafDialectConfig {
	@Bean
	public ThSysDialect thSysDialect() {
		return new ThSysDialect();
	}
}
