package org.geeksexception.project.talent.config;

import org.geeksexception.project.talent.annotation.HerokuProduction;
import org.geeksexception.project.talent.annotation.Production;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

@Configuration
@EnableWebMvc
@Production
@HerokuProduction
public class ProductionWebMvcConfig extends AbstractWebMvcConfig {
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("index");
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/css/**").addResourceLocations("/css/");
		registry.addResourceHandler("/dist/**").addResourceLocations("/dist/");
		registry.addResourceHandler("/sitemap/**").addResourceLocations("/sitemap/");
		registry.addResourceHandler("/img/**").addResourceLocations("/img/");
		registry.addResourceHandler("/fonts/**").addResourceLocations("/fonts/");
		registry.addResourceHandler("/templates/**").addResourceLocations("/templates/");
		registry.addResourceHandler("/js/**").addResourceLocations("/js/");
	}
	
}