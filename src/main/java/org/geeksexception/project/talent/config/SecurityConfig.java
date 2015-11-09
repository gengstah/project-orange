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
				.antMatchers(
						"/api/service/user/authenticate", 
						"/api/service/user/registerTalent", 
						"/api/service/user/registerAgency", 
						"/api/service/user/uploadImage", 
						"/api/service/user/deleteImage/**", 
						"/img/**", 
						"/img/temp/**").permitAll()
				.antMatchers("/api/service/user/**", "/img/talents/**").authenticated()
				.antMatchers("/api/service/user/deleteSavedImage/**").hasRole("USER")
				.antMatchers("/api/service/user/profile").hasAnyRole("AGENCY", "USER")
				.antMatchers("/api/service/user/updateTalent").hasAnyRole("ADMIN", "USER")
				.antMatchers(
						"/api/service/talent/approved**", 
						"/api/service/user/updateAgency").hasAnyRole("ADMIN", "AGENCY")
				.antMatchers(
						"/api/service/talent/**",
						"/api/service/agency/**",
						"/api/service/talent/forApproval**", 
						"/api/service/talent/denied**", 
						"/api/service/agency/forApproval**", 
						"/api/service/agency/denied**", 
						"/api/service/agency/approved**").hasRole("ADMIN")
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