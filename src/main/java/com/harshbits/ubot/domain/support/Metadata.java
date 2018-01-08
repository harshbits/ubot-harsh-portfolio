
package com.harshbits.ubot.domain.support;

import java.io.Serializable;

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
public class Metadata implements Serializable {

	private final static long serialVersionUID = 1L;

	@JsonProperty("intentId")
	private String intentId;

	@JsonProperty("webhookUsed")
	private String webhookUsed;

	@JsonProperty("webhookForSlotFillingUsed")
	private String webhookForSlotFillingUsed;

	@JsonProperty("intentName")
	private String intentName;

	public Metadata withIntentId(String intentId) {
		this.intentId = intentId;
		return this;
	}

	public Metadata withWebhookUsed(String webhookUsed) {
		this.webhookUsed = webhookUsed;
		return this;
	}

	public Metadata withWebhookForSlotFillingUsed(String webhookForSlotFillingUsed) {
		this.webhookForSlotFillingUsed = webhookForSlotFillingUsed;
		return this;
	}

	public Metadata withIntentName(String intentName) {
		this.intentName = intentName;
		return this;
	}

}
