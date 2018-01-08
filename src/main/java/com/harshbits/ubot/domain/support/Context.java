
package com.harshbits.ubot.domain.support;

import java.io.Serializable;
import java.util.HashMap;

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
public class Context implements Serializable {

	private final static long serialVersionUID = 1L;

	@JsonProperty("name")
	private String name;

	@JsonProperty("parameters")
	private HashMap<String, Object> parameters;

	@JsonProperty("lifespan")
	private long lifespan;

	public Context withName(String name) {
		this.name = name;
		return this;
	}

	public Context withParameters(HashMap<String, Object> parameters) {
		this.parameters = parameters;
		return this;
	}

	public Context withLifespan(long lifespan) {
		this.lifespan = lifespan;
		return this;
	}

}
