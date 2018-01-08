package com.harshbits.ubot.domain.support;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

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
public class Fulfillment implements Serializable {

	private final static long serialVersionUID = 1L;

	@JsonProperty("speech")
	private String speech;

	@JsonProperty("source")
	private String source;

	@JsonProperty("displayText")
	private String displayText;

	@JsonProperty("messages")
	private List<Message> messages;

	public Fulfillment withSpeech(String speech) {
		this.speech = speech;
		return this;
	}

	public Fulfillment withSource(String source) {
		this.source = source;
		return this;
	}

	public Fulfillment withDisplayText(String displayText) {
		this.displayText = displayText;
		return this;
	}

	public Fulfillment withMessages(List<Message> messages) {
		this.messages = messages;
		return this;
	}

}
