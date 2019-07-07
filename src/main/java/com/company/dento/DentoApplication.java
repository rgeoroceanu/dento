package com.company.dento;

import com.company.dento.dao.PageableRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
@EnableJpaRepositories(repositoryBaseClass = PageableRepositoryImpl.class)
@EnableTransactionManagement
@EntityScan({"com.company.dento.model.business", "com.company.dento.model.converter"})
public class DentoApplication  extends SpringBootServletInitializer {
	
	public static void main(String[] args) {
		SpringApplication.run(DentoApplication.class, args);
	}
}
