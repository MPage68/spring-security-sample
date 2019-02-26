package com.page.springsecuritysample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.csrf().disable()
		.authorizeRequests()	
			.anyRequest()
			.authenticated()
			.and()
		.formLogin()
			.loginPage("/login").permitAll()
			.usernameParameter("username")
			.passwordParameter("password");
	}

	@Bean
	public JdbcUserDetailsManager userDetailsManager() {
		JdbcUserDetailsManager manager = new JdbcUserDetailsManager();
		manager.setDataSource(jdbcTemplate.getDataSource());
		manager.setUsersByUsernameQuery("select username,password,enabled from users where username=?");
		manager.setAuthoritiesByUsernameQuery("select username,authority from authorities where username=?");
		return manager;
	}
	
	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder builder) throws Exception {
		builder.userDetailsService(userDetailsManager());
	}
}