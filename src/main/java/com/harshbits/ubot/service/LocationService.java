package com.harshbits.ubot.service;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.AddressComponent;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.harshbits.ubot.domain.LocationRequest;
import com.harshbits.ubot.domain.LocationResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LocationService {
	
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
			String cityName = "Unknown";
			GeoApiContext context = new GeoApiContext.Builder().apiKey(System.getenv("location-api-key")).build();
			GeocodingResult[] results;
			//Get Reverse Geocode and Location
			results = GeocodingApi.reverseGeocode(context, new LatLng(request.getLatitude(), request.getLongitude()))
					.await();
			//Address Components
			AddressComponent[] components = results[0].addressComponents;
			log.info("Address Components: {}", new Gson().toJson(components));
			if(components != null && components.length > 0) {
				for(AddressComponent component : components) {
					for(AddressComponentType type: component.types) {
						if(type == AddressComponentType.LOCALITY) {
							cityName = component.longName;
						}
					}
				}
			}
			response.setCity(cityName);
			return response;
		} catch (Exception e) {
			log.error("Get city name error: {}", e);
			response.setCity("Unknown");
			return response;
		}
	}
	
}
