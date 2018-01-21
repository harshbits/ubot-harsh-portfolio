package com.harshbits.ubot.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.fedy2.weather.YahooWeatherService;
import com.github.fedy2.weather.data.Channel;
import com.github.fedy2.weather.data.Condition;
import com.github.fedy2.weather.data.Forecast;
import com.github.fedy2.weather.data.unit.DegreeUnit;
import com.harshbits.ubot.constants.UbotConstants;
import com.harshbits.ubot.domain.LocationResponse;
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

	protected static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	protected static final SimpleDateFormat sdfDay = new SimpleDateFormat("EEEE");

	private static final ThreadLocal<SimpleDateFormat> dayFormatter = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return sdfDay;
		}
	};

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
		if (action.equals(UbotConstants.WEATHER_INTENT) || action.equals(UbotConstants.WEATHER_TEMPERATURE_INTENT)) {
			if (location != null && !location.isEmpty()) {
				DegreeUnit degreeUnit = getDegreeUnit(request);
				return getTemperatureResponse(location, date, degreeUnit);
			}
		}
		// Process Activity related intent
		else if (action.equals(UbotConstants.WEATHER_ACTIVITY_INTENT)) {
			String activity = getActivity(request);
			if (activity != null && !activity.isEmpty()) {
				return getActivityResponse(request, date, activity);
			}
		}
		// Process Condition related intent
		else if (action.equals(UbotConstants.WEATHER_CONDITION_INTENT)) {
			String condition = request.getResult().getParameters().getCondition();
			DegreeUnit degreeUnit = getDegreeUnit(request);
			return getConditionResponse(location, date, degreeUnit, condition);
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
						output = "Tomorrow in " + city + " it will be " + condition + ", expected high of " + highTemp
								+ " degree " + unitValue.toLowerCase() + " and low of " + lowTemp + " degree "
								+ unitValue.toLowerCase();
					} else {
						Forecast forecast = channel.getItem().getForecasts().stream()
								.filter(f -> DateUtils.isSameDay(f.getDate(), date)).collect(Collectors.toList())
								.get(0);
						String unitValue = channel.getUnits().getTemperature().name();
						String condition = forecast.getText();
						int highTemp = forecast.getHigh();
						int lowTemp = forecast.getLow();
						
						if(!isDateInCurrentWeek(date)) {
							output = "Next "+ dayFormatter.get().format(date) + " in " + city + " it will be " + condition
									+ ", expected high of " + highTemp + " degree " + unitValue.toLowerCase()
									+ " and low of " + lowTemp + " degree " + unitValue.toLowerCase();
						}else {
							output = dayFormatter.get().format(date) + " in " + city + " it will be " + condition
									+ ", expected high of " + highTemp + " degree " + unitValue.toLowerCase()
									+ " and low of " + lowTemp + " degree " + unitValue.toLowerCase();
						}
					}
				}
			}
		} catch (JAXBException | IOException e) {
			log.error("Get temperature error {}", e.getMessage());
		}
		return output;
	}
	
	
	public LocationResponse getWeatherCondition(LocationResponse location) {
		try {
			String loc = location.getCity() + ", " + location.getStateCode();
			Channel channel = yahooWeatherService.getForecastForLocation(loc, DegreeUnit.FAHRENHEIT).all().get(0);
			Condition condition = channel.getItem().getCondition();
			int temperature = channel.getItem().getCondition().getTemp();
			// Unit
			// String unitValue = channel.getUnits().getTemperature().name();
			location.setTemperature(temperature);
			location.setCondition(condition.getText());
			location.setConditionCode(condition.getCode());
			location.setUnit("F");
		} catch (Exception e) {
			log.error("Fetching weather condition error: {}", e);
		}
		return location;
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
	 * Get response for weather.activity action, such as rain, snow,
	 * etc.
	 * 
	 * @param request
	 * @return
	 */
	private String getConditionResponse(String location, Date date, DegreeUnit unit, String condition) {
		String output = "Sorry, I can't predict that for now!";
		try {
//			Channel channel = yahooWeatherService.getForecastForLocation(location, unit).all().get(0);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

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
			} else if (StringUtils.isNotBlank(unitCode) && unitCode.equals("C")) {
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
			// Get City
//			city = request.getResult().getParameters().getAddress().getCity();

			// Default city - Configured
//			if (StringUtils.isBlank(city)) {
				city = (String) request.getResult().getContexts().get(0).getParameters().get("geo-city");
//			}
		} catch (Exception e) {
			log.error("Handle webhook request error {}", e);
		}
		return city;
	}
	
	/**
	 * This method determines, whether the day is in current week or not.
	 * 
	 * @param date
	 * @return true if date is in current week.
	 */
	private boolean isDateInCurrentWeek(Date date) {
		Calendar currentCalendar = Calendar.getInstance();
		int week = currentCalendar.get(Calendar.WEEK_OF_YEAR);
		int year = currentCalendar.get(Calendar.YEAR);
		Calendar targetCalendar = Calendar.getInstance();
		targetCalendar.setTime(date);
		int targetWeek = targetCalendar.get(Calendar.WEEK_OF_YEAR);
		int targetYear = targetCalendar.get(Calendar.YEAR);
		return week == targetWeek && year == targetYear;
	}
}
