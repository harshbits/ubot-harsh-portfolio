
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
public class Status implements Serializable {

	private final static long serialVersionUID = 1L;

	@JsonProperty("code")
	private long code;

	@JsonProperty("errorType")
	private String errorType;

	public Status withCode(long code) {
		this.code = code;
		return this;
	}

	public Status withErrorType(String errorType) {
		this.errorType = errorType;
		return this;
	}

}
