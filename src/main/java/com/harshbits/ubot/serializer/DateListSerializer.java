package com.harshbits.ubot.serializer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class DateListSerializer extends JsonSerializer<List<Date>> {

	private static String dateFormatter = "yyyy-MM-dd";

	@Override
	public void serialize(List<Date> value, JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonProcessingException {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormatter);
		jgen.writeStartArray();
		for (Date date : value) {
			jgen.writeStartObject();
			jgen.writeString(sdf.format(date));
			jgen.writeEndObject();
		}
		jgen.writeEndArray();
	}
	
	public String getDateFormatter() {
		return dateFormatter;
	}

	public void setDateFormatter(String dateFormatter) {
		if (dateFormatter != null) {
			DateListSerializer.dateFormatter = dateFormatter;
		}
	}

	

}