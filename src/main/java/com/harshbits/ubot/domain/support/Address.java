package com.harshbits.ubot.domain.support;

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
public class Address {

	@JsonProperty("city.original")
	private String cityOriginal;

	@JsonProperty("city")
	private String city;
	
	@JsonProperty("zip-code.original")
	private String zipCodeOriginal;

	@JsonProperty("zip-code")
	private String zipCode;
	
	private String address;
	
}
