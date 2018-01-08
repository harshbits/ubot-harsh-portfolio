
package com.harshbits.ubot.domain.support;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.harshbits.ubot.serializer.AddressDeserializer;
import com.harshbits.ubot.serializer.DateListDeserializer;
import com.harshbits.ubot.serializer.DateListSerializer;

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
public class Parameters implements Serializable {

	private final static long serialVersionUID = 1L;

	@JsonProperty("date-time.original")
	private String dateTimeOriginal;

	@JsonProperty("unit.original")
	private String unitOriginal;

	@JsonProperty("unit")
	private String unit;
	
	@JsonProperty("address")
	@JsonDeserialize(using = AddressDeserializer.class)
	private Address address;

	@JsonProperty("geo-city")
	private String geoCity;

	@JsonProperty("date-time")
	@JsonSerialize(using = DateListSerializer.class)
	@JsonDeserialize(using = DateListDeserializer.class)
	private List<Date> dateTime;

	@JsonProperty("geo-city.original")
	private String geoCityOriginal;

	@JsonProperty("address.original")
	private String addressOriginal;

	@JsonProperty("condition")
	private String condition;
}
