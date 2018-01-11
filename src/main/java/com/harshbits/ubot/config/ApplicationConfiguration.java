package com.harshbits.ubot.config;

import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.fedy2.weather.YahooWeatherService;
import com.google.maps.GeoApiContext;
import com.harshbits.ubot.config.IntentConfiguration.IgnoreIntents;

import ai.api.AIConfiguration;
import ai.api.AIDataService;

@Configuration
public class ApplicationConfiguration {
	
	@Value("${dialog-flow.auth-token}")
	private String dialogFlowAuthToken; 
	
	@Bean
	public AIDataService dataService() {
		AIConfiguration configuration = new AIConfiguration(dialogFlowAuthToken);
		return new AIDataService(configuration);
	}
	
	/**
	 * Yahoo Weather Service Bean class
	 * 
	 * @return
	 * @throws JAXBException
	 */
	@Bean
	public YahooWeatherService yahooWeatherService() throws JAXBException {
		return new YahooWeatherService();
	}
	
	@Bean
	public IgnoreIntents ignoreIntents() {
		return new IgnoreIntents();
	}
	
	@Bean
	public GeoApiContext geoApiContext() {
		GeoApiContext context = new GeoApiContext.Builder().apiKey(System.getenv("location-api-key")).build();
		return context;
	}
}
