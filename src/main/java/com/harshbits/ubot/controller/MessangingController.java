package com.harshbits.ubot.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.harshbits.ubot.domain.LocationRequest;
import com.harshbits.ubot.domain.LocationResponse;
import com.harshbits.ubot.domain.UbotMessage;
import com.harshbits.ubot.service.LocationService;
import com.harshbits.ubot.service.MessangingService;

@RestController
@RequestMapping("v1")
public class MessangingController {

	@Autowired
	private MessangingService messangingService;
	
	@Autowired
	private LocationService locationService;
	
	/**
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/message", method = RequestMethod.POST, 
		produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, 
		headers = "Accept=application/json")
	public UbotMessage processMessages(@RequestBody UbotMessage request) throws Exception {
		return messangingService.processMessage(request);
	}
	
	/**
	 * 
	 * @param request is an instance of {@link LocationRequest}
	 * @return
	 * @throws Exception
	 */
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "location", method = RequestMethod.POST, 
		produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, 
		headers = "Accept=application/json")
	public LocationResponse getCityName(@RequestBody @Valid LocationRequest request) throws Exception {
		return locationService.getCityName(request);
	}
	
}
