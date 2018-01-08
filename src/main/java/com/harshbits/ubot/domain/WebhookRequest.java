
package com.harshbits.ubot.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.harshbits.ubot.domain.support.Result;
import com.harshbits.ubot.domain.support.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebhookRequest implements Serializable {

	private final static long serialVersionUID = 1L;

	@JsonProperty("id")
	private String id;

	@JsonProperty("timestamp")
	private String timestamp;

	@JsonProperty("lang")
	private String lang;

	@JsonProperty("result")
	private Result result;

	@JsonProperty("status")
	private Status status;

	@JsonProperty("sessionId")
	private String sessionId;

}