package org.geeksexception.project.talent.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

@Configuration
@EnableTransactionManagement
@ComponentScan("org.geeksexception.project.talent.service")
public class ServiceConfig {
	
	@Bean
	public AmazonS3 s3Client() {
		
		AmazonS3 s3Client = new AmazonS3Client();
		s3Client.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_1));
		
		return s3Client;
		
	}
	
	@Bean
	public String bucketName() {
		
		return System.getenv("BUCKET_NAME");
		
	}
	
}