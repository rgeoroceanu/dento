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

@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.csrf().disable()
		//.requireCsrfProtectionMatcher(new AntPathRequestMatcher("**/login.html"))
		//.and()
		.authorizeRequests()
		.anyRequest().authenticated()
		.and()
		.formLogin()
		.usernameParameter("username")
		.passwordParameter("password")
		//.defaultSuccessUrl("/")
		.loginProcessingUrl("/login")
		.loginPage("/login.html")
				.permitAll()
		.and()
		.logout()
				.permitAll()
		.logoutSuccessUrl("/login.html")
		.deleteCookies("JSESSIONID")
		.invalidateHttpSession(true); 
	}

	@Override 
	public void configure(WebSecurity web) throws Exception { 
		web.ignoring().antMatchers("/*.css");
		web.ignoring().antMatchers("/*.js");
		web.ignoring().antMatchers("/VAADIN/**");
		web.ignoring().antMatchers("login.html");
		web.ignoring().antMatchers("logout.html");
		web.ignoring().antMatchers("/logincss/style.css");
	}

	@Autowired
	public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
		auth
				//.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
				//.withUser("demouser").password("$2a$04$CBxD9.eUBMVCS2zGbBUDqOpkjsw/odYGe0Y4wEXbMnVjsGLSul0b2").roles("ADMIN", "USER");
		.userDetailsService(userDetailsService)
		.passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}