package org.geeksexception.project.talent.config;

import org.geeksexception.project.talent.annotation.Development;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath:dev/server.xml")
@Development
public class DevelopmentWebServiceConfig {
	
}