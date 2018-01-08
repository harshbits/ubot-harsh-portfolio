
package com.harshbits.ubot.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.gson.annotations.SerializedName;
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

	@SerializedName("id")
	private String id;

	@SerializedName("timestamp")
	private String timestamp;

	@SerializedName("lang")
	private String lang;

	@SerializedName("result")
	private Result result;

	@SerializedName("status")
	private Status status;

	@SerializedName("sessionId")
	private String sessionId;

}