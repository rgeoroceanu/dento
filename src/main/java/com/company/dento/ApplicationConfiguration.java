package com.company.dento;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import com.company.dento.ui.localization.Localizer;

@Configuration
public class ApplicationConfiguration {

	@Bean
	@Scope("singleton")
	public Localizer localizer() {
		return Localizer.getInstance();
	}

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource ret = new ReloadableResourceBundleMessageSource();
		ret.setBasename("classpath:localization/loca");
		ret.setDefaultEncoding("UTF-8");
		return ret;
	}

	@Bean
	public String version() {
		return "0.0.1-SNAPSHOT";
	}
}
