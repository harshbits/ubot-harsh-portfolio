package com.harshbits.ubot.domain;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * This is UBot Message (for chat) domain object, which uses for chat integration.
 * <p>
 * 
 * @author harshbhavsar
 * @since 1.0.0
 *
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UbotMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull(message = "Missing required field author.")
	private String author;
	
	@NotNull(message = "Missing required field type.")
	private String type;

	@Valid
	@NotNull(message = "Missing required field data.")
	private UbotData data;

}
