package com.harshbits.ubot.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harshbits.ubot.config.IntentConfiguration.IgnoreIntents;
import com.harshbits.ubot.domain.UbotData;
import com.harshbits.ubot.domain.UbotMessage;

import ai.api.AIDataService;
import ai.api.AIServiceException;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MessangingService {

	@Autowired
	private AIDataService dataService;
	
	@Autowired
	private IgnoreIntents ignoreIntents;

	public UbotMessage processMessage(UbotMessage request) {
		
		UbotMessage response = new UbotMessage();
		response.setAuthor("them");
		response.setType("text");
		log.info("Request {}", request);

		//Prepare AIRequest
		AIRequest aiRequest = new AIRequest(request.getData().getText());
		try {
			AIResponse aiResponse = dataService.request(aiRequest);
			UbotData data = new UbotData();
			if (aiResponse.getStatus().getCode() == 200) {
				String action = aiResponse.getResult().getAction().trim();
				log.info("Input Action:- "+ action);
				if (action.trim().startsWith(ignoreIntents.getIgnoreIntents())) {
					String parsedResponse = aiResponse.getResult().getFulfillment().getSpeech();
					
					if(StringUtils.isNotBlank(parsedResponse)){
						log.info(parsedResponse);
						data.setText(parsedResponse);
					}else{
						data.setText("Sorry, I am unable to reply that.");
					}
				}
			}
			
			response.setData(data);
		} catch (AIServiceException e) {
			log.error("Dialog Flow Error: {}", e);
		}
		return response;
	}

}
