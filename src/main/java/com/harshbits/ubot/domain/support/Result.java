
package com.harshbits.ubot.domain.support;

import java.io.Serializable;
import java.util.List;

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
public class Result implements Serializable {
	
	private final static long serialVersionUID = 1L;

	@JsonProperty("source")
	private String source;

	@JsonProperty("resolvedQuery")
	private String resolvedQuery;

	@JsonProperty("action")
	private String action;

	@JsonProperty("actionIncomplete")
	private boolean actionIncomplete;

	@JsonProperty("parameters")
	private Parameters parameters;

	@JsonProperty("contexts")
	private List<Context> contexts;

	@JsonProperty("metadata")
	private Metadata metadata;

	@JsonProperty("fulfillment")
	private Fulfillment fulfillment;

	@JsonProperty("score")
	private long score;

}
