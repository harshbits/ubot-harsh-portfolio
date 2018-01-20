package com.harshbits.ubot.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.AddressComponent;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.harshbits.ubot.config.FontIconMapper;
import com.harshbits.ubot.domain.LocationRequest;
import com.harshbits.ubot.domain.LocationResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LocationService {
	
	@Autowired
	private GeoApiContext geoApiContext;
	
	@Autowired
	private WeatherService weatherService;
	
	@Autowired
	private FontIconMapper fontIconMapper;
	
	/**
	 * This method returns instance of {@link LocationResponse},
	 * which contains city name.
	 * <p>
	 * @param request
	 * @return an instance of {@link LocationResponse}
	 */
	public LocationResponse  getCityName(LocationRequest request) {
		LocationResponse response = new LocationResponse();
		try {
			//Default name unknown
			GeocodingResult[] results;
			//Get Reverse Geocode and Location
			results = GeocodingApi.reverseGeocode(geoApiContext, new LatLng(request.getLatitude(), request.getLongitude()))
					.await();
			//Address Components
			AddressComponent[] components = results[0].addressComponents;
			log.info("Address Components: {}", new Gson().toJson(components));
			if(components != null && components.length > 0) {
				for(AddressComponent component : components) {
					for(AddressComponentType type: component.types) {
						if(type == AddressComponentType.LOCALITY) {
							response.setCity(component.longName);
						}
						if(type == AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_1) {
							response.setState(component.longName);
							response.setStateCode(component.shortName);
						}
						if(type == AddressComponentType.COUNTRY) {
							response.setCountry(component.longName);
							response.setCountryCode(component.shortName);
						}
						
						// Get weather information
						if(!StringUtils.isBlank(response.getCity())) {
							weatherService.getWeatherCondition(response);	
							// Assign icon
							response.setWeatherIcon(fontIconMapper.getFontIcon(response.getConditionCode()));
						}
					}
				}
			}
			return response;
		} catch (Exception e) {
			log.error("Get city name error: {}", e);
			return response;
		}
	}
	
}
