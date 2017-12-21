package com.harshbits.ubot;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.harshbits.ubot.filter.RequestResponseLoggingFilter;

@ComponentScan
@EnableAutoConfiguration
@SpringBootApplication
@EnableConfigurationProperties
public class UbotHarshPortfolioApplication {
	
	@Value("${http-logging.ignore-response-methods:NONE}")
	private String ignoreResponseMethods; 
	
	
	@Value("${http-logging.url_patterns}")
	private String logURLPatterns; 

	public static void main(String[] args) {
		SpringApplication.run(UbotHarshPortfolioApplication.class, args);
	}
	
	/**
	 * Request Response Logging Filter bean class Declaration.
	 * <p>
	 * @return
	 */
	@Bean
	public RequestResponseLoggingFilter requestResponseLoggingFilter(){
		return new RequestResponseLoggingFilter();
	}
	
	/**
	 * Filter Registration Bean class to filter URI's starts with <b>version</b>.
	 * <p>
	 * @return {@link FilterRegistrationBean}
	 */
	@Bean
	public FilterRegistrationBean loggingFilter() {
		FilterRegistrationBean filterRegBean = new FilterRegistrationBean();
		filterRegBean.setFilter(requestResponseLoggingFilter());
		filterRegBean.setUrlPatterns(Arrays.asList(logURLPatterns.split(",")));
		filterRegBean.addInitParameter("ignoreResponseMethods", ignoreResponseMethods);
		return filterRegBean;
	}
}
