package com.harshbits.ubot.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.harshbits.ubot.domain.UbotData;
import com.harshbits.ubot.domain.UbotMessage;

import ai.api.AIDataService;
import ai.api.AIServiceException;
import ai.api.RequestExtras;
import ai.api.model.AIContext;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Location;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MessangingService {

	@Autowired
	private AIDataService dataService;

	// @Autowired
	// private IgnoreIntents ignoreIntents;

	public UbotMessage processMessage(UbotMessage request) {

		UbotMessage response = new UbotMessage();
		response.setAuthor("them");
		response.setType("text");
		log.info("Request {}", request);

		// Prepare AIRequest
		AIRequest aiRequest = new AIRequest(request.getData().getText());
		aiRequest.setLocation(new Location(40.7128, 74.0060));
//		
//		
		List<AIContext> contexts = new ArrayList<>();
		AIContext context = new AIContext();
		context.setName("weather");
		Map<String, String> parameters = new HashMap<>();
//		parameters.put("geo-city", "new york");
		parameters.put("location", "new york");
//		parameters.put("address.original", "new york");
//		parameters.put("address", "{\n" + 
//				"        \"city\": \"New York\"\n" + 
//				"      }");
		context.setParameters(parameters);
		contexts.add(context);
		aiRequest.setContexts(contexts);
//		
		aiRequest.addContext(context);
		try {
//			AIServiceContext customContext = AIServiceContextBuilder.buildFromSessionId("customSessionId");
			
			
			RequestExtras extras = new RequestExtras();
			extras.setLocation(new Location(40.7128, 74.0060));
			extras.setContexts(contexts);
			
			AIResponse aiResponse = dataService.request(aiRequest, extras);
			log.info("AI Response: {}", new Gson().toJson(aiResponse));
			UbotData data = new UbotData();
			if (aiResponse.getStatus().getCode() == 200) {
				String action = aiResponse.getResult().getAction().trim();
				log.info("Input Action:- " + action);
				String parsedResponse = aiResponse.getResult().getFulfillment().getSpeech();
				if (StringUtils.isNotBlank(parsedResponse)) {
					log.info(parsedResponse);
					data.setText(parsedResponse);
				} else {
					data.setText("Sorry, I am unable to reply that.");
				}
			}
			response.setData(data);
		} catch (AIServiceException e) {
			log.error("Dialog Flow Error: {}", e);
		}
		return response;
	}

}
