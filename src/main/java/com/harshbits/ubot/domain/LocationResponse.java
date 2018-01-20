package com.harshbits.ubot.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LocationResponse implements Serializable {

	private final static long serialVersionUID = 1L;
	
	private String city;
	
	private String state;
	
	private String stateCode;
	
	private String country;
	
	private String countryCode;
	
	private Integer temperature;
	
	private String unit;
	
	private String condition;

}
