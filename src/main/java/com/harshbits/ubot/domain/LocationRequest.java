package com.harshbits.ubot.domain;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LocationRequest implements Serializable {

	private final static long serialVersionUID = 1L;
	
	@NotNull(message = "Required parameter latitude")
	private Double latitude;
	
	@NotNull(message = "Required parameter longitude")
	private Double longitude;

}
