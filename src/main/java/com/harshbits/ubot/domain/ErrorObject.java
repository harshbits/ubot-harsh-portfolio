package com.harshbits.ubot.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorObject implements Serializable {

	private static final long serialVersionUID = 1L;

	private String code;

	private String message;

	private String message_details;

	/**
	 * Constructs for error code and message and message_details
	 * 
	 * @param code
	 *            Error code
	 * @param message
	 *            Error message
	 */
	public ErrorObject(String code, String message) {
		this.code = code;
		this.message = message;
	}

}
