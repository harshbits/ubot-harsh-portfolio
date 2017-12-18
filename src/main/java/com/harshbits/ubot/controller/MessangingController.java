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

import com.harshbits.ubot.domain.UbotMessage;
import com.harshbits.ubot.service.MessangingService;

@RestController
@RequestMapping("v1/message")
public class MessangingController {

	@Autowired
	private MessangingService messangingService;
	
	/**
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, 
		consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, headers = "Accept=application/json")
	public UbotMessage processMessages(@RequestBody @Valid UbotMessage request) throws Exception {
		return messangingService.processMessage(request);
	}
	
}
