package org.geeksexception.project.talent.config;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private @Inject UserDetailsService userDetailsService;
	
	public SecurityConfig() { }
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder(11));
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		/* Dev */
//		http
//		.authorizeRequests()
//			.antMatchers("/api/service/**/**").permitAll()
//			.and()
//		.csrf().disable();
		
		/* Prod */
		http
			.authorizeRequests()
				.antMatchers("/api/service/user/authenticate", "/api/service/user/register").permitAll()
				.antMatchers("/api/service/user/**", "/img/**").authenticated()
				.antMatchers("/api/service/admin/**").hasRole("ADMIN")
				.and()
			.csrf().disable()
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
		
	}
	
	@Bean(name="authenticationManager")
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
}