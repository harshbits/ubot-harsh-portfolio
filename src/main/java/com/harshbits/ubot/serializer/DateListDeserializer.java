package com.harshbits.ubot.serializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class DateListDeserializer extends JsonDeserializer<List<Date>> {

	private static String dateFormatter = "yyyy-MM-dd";

	private static final TypeReference<List<String>> listType = new TypeReference<List<String>>() {
	};

	@Override
	public List<Date> deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {

		SimpleDateFormat sdf = new SimpleDateFormat(dateFormatter);
		List<Date> deserializedDates = new ArrayList<>();
		List<String> dateList = new ArrayList<>();
		try {
			dateList = jp.readValueAs(listType);
		} catch (Exception e) {
			// not list, then consider it as string
			dateList.add(jp.readValueAs(String.class));
		}
		if (dateList != null && dateList.size() > 0) {
			for (String value : dateList) {
				try {
					deserializedDates.add(sdf.parse(value));
				} catch (ParseException e) {
				}
			}
			return deserializedDates;
		}
		return null;
	}

	public String getDateFormatter() {
		return dateFormatter;
	}

	public void setDateFormatter(String dateFormatter) {
		if (dateFormatter != null) {
			DateListDeserializer.dateFormatter = dateFormatter;
		}
	}

}