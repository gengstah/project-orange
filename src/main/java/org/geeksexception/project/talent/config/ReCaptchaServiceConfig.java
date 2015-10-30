package org.geeksexception.project.talent.config;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.geeksexception.project.talent.api.ReCaptchaManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

@Configuration
@PropertySource("classpath:/reCaptcha.properties")
public class ReCaptchaServiceConfig {
	
	private static final List<Object> providers;
	
	private @Inject Environment env;
	
	static {
		providers = getProviders();
	}
	
	private static List<Object> getProviders() {
		
		List<Object> providers = new ArrayList<Object>();
		providers.add(new JacksonJsonProvider());
		
		return providers;
		
	}
	
	public ReCaptchaServiceConfig() { }
	
	@Bean
	public ReCaptchaManager reCaptchaManager() {
		return JAXRSClientFactory.create(env.getProperty("verification.url"), ReCaptchaManager.class, providers, true);
	}
	
}