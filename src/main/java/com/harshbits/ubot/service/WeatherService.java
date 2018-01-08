package com.harshbits.ubot.service;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.fedy2.weather.YahooWeatherService;
import com.github.fedy2.weather.data.Channel;
import com.github.fedy2.weather.data.unit.DegreeUnit;
import com.harshbits.ubot.constants.IntentConstant;
import com.harshbits.ubot.domain.WebhookRequest;

import lombok.extern.slf4j.Slf4j;

/**
 * This service handles all weather/outdoor activities related webhook requests
 * 
 * @author harshbhavsar
 *
 */
@Service
@Slf4j
public class WeatherService {

	@Autowired
	private YahooWeatherService yahooWeatherService;
	
//	protected static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//	
//	private static final ThreadLocal<SimpleDateFormat> formatter = new ThreadLocal<SimpleDateFormat>() {
//		@Override
//		protected SimpleDateFormat initialValue() {
//			return sdf;
//		}
//	};

	/**
	 * This method handles Webhook request (which comes from api.ai response) and
	 * process it based on intent.
	 * 
	 * @param request
	 * @return
	 */
	public String handleRequest(WebhookRequest request, String action) {

		// Process the request and get city/location
		String location = processRequestToGetCity(request);

		// Process the request and get requested date
		Date date = determineDate(request);

		// Process Temperature/weather condition related request
		if (action.equals(IntentConstant.WEATHER_INTENT) || action.equals(IntentConstant.WEATHER_TEMPERATURE_INTENT)) {
			if (location != null && !location.isEmpty()) {
				DegreeUnit degreeUnit = getDegreeUnit(request);
				return getTemperatureResponse(location, date, degreeUnit);
			}
		}
		// Process Activity related intent
		else if (action.equals(IntentConstant.WEATHER_ACTIVITY_INTENT)) {
			String activity = getActivity(request);
			if (activity != null && !activity.isEmpty()) {
				return getActivityResponse(request, date, activity);
			}
		}
		return "";
	}

	/**
	 * Get temperature for the particular location in requested unit (C or F) format
	 * 
	 * @param location
	 * @param unit
	 * @return
	 */
	private String getTemperatureResponse(String location, Date date, DegreeUnit unit) {
		String output = "";
		// Yahoo weather service has this format as date
		try {
			Channel channel = yahooWeatherService.getForecastForLocation(location, unit).all().get(0);

			if (channel != null) {
				String city = channel.getLocation().getCity();
				if (DateUtils.isSameDay(date, Calendar.getInstance().getTime())) {
					String condition = channel.getItem().getCondition().getText();
					int temperature = channel.getItem().getCondition().getTemp();
					String unitValue = channel.getUnits().getTemperature().name();
					output = "Today in " + city + " it is " + condition + ", the temperature is " + temperature
							+ " degree " + unitValue.toLowerCase();
				} else {
					final Calendar cal = Calendar.getInstance();
					cal.add(Calendar.DATE, 1);
					// If tomorrow
					if (DateUtils.isSameDay(date, cal.getTime())) {
						String condition = channel.getItem().getForecasts().get(1).getText();
						int highTemp = channel.getItem().getForecasts().get(1).getHigh();
						int lowTemp = channel.getItem().getForecasts().get(1).getLow();
						String unitValue = channel.getUnits().getTemperature().name();
						output = "Tomorrow in " + city + " it will be " + condition + ", expected high of "
								+ highTemp + " degree " + unitValue.toLowerCase() + " and low of "
								+ lowTemp + " degree " + unitValue.toLowerCase();
					}
				}
			}
		} catch (JAXBException | IOException e) {
			log.error("Get temperature error {}", e.getMessage());
		}
		return output;
	}

	/**
	 * Get response for weather.activity action, such as running, going out skiing,
	 * etc.
	 * 
	 * @param request
	 * @return
	 */
	private String getActivityResponse(WebhookRequest request, Date date, String activity) {
		String output = "";

		return output;
	}

	/**
	 * Get degree unit as per the webhook request
	 * 
	 * @param request
	 * @return
	 */
	private DegreeUnit getDegreeUnit(WebhookRequest request) {
		// Default degree unit to CELSIUS
		DegreeUnit unit = DegreeUnit.FAHRENHEIT;
		try {
			String unitCode = request.getResult().getParameters().getUnit();
			if (StringUtils.isNotBlank(unitCode) && unitCode.equals("F")) {
				unit = DegreeUnit.FAHRENHEIT;
			}else  if (StringUtils.isNotBlank(unitCode) && unitCode.equals("C")) {
				unit = DegreeUnit.CELSIUS;
			}
		} catch (Exception e) {
			log.error("Get Temperature unit error {}", e.getMessage());
		}
		return unit;
	}

	/**
	 * Get activity from the weather activity intent request
	 * 
	 * @param request
	 * @return
	 */
	private String getActivity(WebhookRequest request) {
		String activity = "";
		try {
			activity = (String) request.getResult().getContexts().get(0).getParameters().get("activity");
		} catch (Exception e) {
			log.error("Get Activity error {}", e.getMessage());
		}
		return activity;
	}

	/**
	 * This method determines date string for the request. It first checks whether
	 * response JSON object from api.ai is array or not, if not than it parses
	 * string as date. In any failure it will return today's date as response
	 * 
	 * @param request
	 * @return
	 */
	private Date determineDate(WebhookRequest request) {
		try {
			if (request.getResult().getParameters().getDateTime().get(0) != null) {
				return request.getResult().getParameters().getDateTime().get(0);
			} else {
				return new Date();
			}
		} catch (Exception e) {
			log.error("Get Date string error {}", e.getMessage());
			return new Date();
		}
	}

	/**
	 * This method processes webhook request and returns appropriate city to process
	 * the request
	 * 
	 * @param request
	 * @return
	 */
	private String processRequestToGetCity(WebhookRequest request) {
		String city = "";
		try {
			//Get City
			city = request.getResult().getParameters().getAddress().getCity();
			
			// Default city - Configured
			if (StringUtils.isBlank(city)) {
				city = (String) request.getResult().getContexts().get(0).getParameters().get("geo-city");
			}
		} catch (Exception e) {
			log.error("Handle webhook request error {}", e);
		}
		return city;
	}
}
