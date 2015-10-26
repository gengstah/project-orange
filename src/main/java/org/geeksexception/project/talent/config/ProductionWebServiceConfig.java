package org.geeksexception.project.talent.config;

import org.geeksexception.project.talent.annotation.HerokuProduction;
import org.geeksexception.project.talent.annotation.Production;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath:server.xml")
@Production
@HerokuProduction
public class ProductionWebServiceConfig {
	
}