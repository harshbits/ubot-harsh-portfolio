package com.harshbits.ubot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.harshbits.ubot.constants.UbotConstants;
import com.harshbits.ubot.domain.WebhookRequest;
import com.harshbits.ubot.domain.WebhookResponse;
import com.harshbits.ubot.service.WeatherService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("v1/webhook")
public class WebhookController {

	@Autowired
	private WeatherService weatherService;
	
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, headers = "Accept=application/json")
	public ResponseEntity<?> webhook(@RequestBody WebhookRequest payload) throws Exception {
		
		WebhookResponse response = new WebhookResponse();
		try {
			assignTaskOnIntent(payload, response);
		} catch (Exception e) {
			log.error("Webhook request controller error {}", e);
		}
		return new ResponseEntity<WebhookResponse>(response, HttpStatus.OK);
	}

	/**
	 * This method assigns appropriate task to request payload based on input intent
	 * 
	 * @param request
	 * @param response
	 */
	private void assignTaskOnIntent(WebhookRequest request, WebhookResponse response) {

		String action = request.getResult().getAction().toLowerCase();

		log.info("Request Action: {}", action);
		String responseString = "";

		if (action != null) {
			// For weather related intent processing
			if (action.startsWith(UbotConstants.WEATHER_INTENT)) {
				responseString = weatherService.handleRequest(request, action);
			}
		}
		log.info("Response Speech Text {}", responseString);
		response.setSpeech(responseString);
		response.setDisplayText("Action triggered: " + action);
	}

}