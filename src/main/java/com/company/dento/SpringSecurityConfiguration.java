package com.company.dento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.csrf()
		.requireCsrfProtectionMatcher(new AntPathRequestMatcher("**/login.html"))
		.and()
		.authorizeRequests()
		.antMatchers("/cms**").hasAnyAuthority("USER,TECHNICIAN,ADMIN")
		.and()
		.formLogin()
		.usernameParameter("username")
		.passwordParameter("password")
		.defaultSuccessUrl("/cms") 
		.loginProcessingUrl("/login")
		.loginPage("/login.html")
		.and()
		.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		.logoutSuccessUrl("/login.html")
		.deleteCookies("JSESSIONID")
		.invalidateHttpSession(true); 
	}

	@Override 
	public void configure(WebSecurity web) throws Exception { 
		web.ignoring().antMatchers("/*.css"); 
		web.ignoring().antMatchers("/*.js"); 
		web.ignoring().antMatchers("/VAADIN/**");
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
		.userDetailsService(userDetailsService)
		.passwordEncoder(passwordEncoder());
	}

	@Bean 
	public PasswordEncoder passwordEncoder() { 
		PasswordEncoder encoder = new BCryptPasswordEncoder(); 
		return encoder; 
	}
}