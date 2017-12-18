package com.harshbits.ubot.domain;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This is UBot Data (part of chat) domain object, which uses for chat integration.
 * <p>
 * 
 * @author harshbhavsar
 * @since 1.0.0
 *
 * @see {@link UbotMessage}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UbotData implements Serializable{

	private static final long serialVersionUID = 1L;

	private String code;
	
	@NotNull(message = "Missing required field text.")
	private String text;

}
