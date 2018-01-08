package com.harshbits.ubot.serializer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class DateSerializer extends JsonSerializer<Date> {

	private static String dateFormatter = "yyyy-MM-dd";

	@Override
	public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonProcessingException {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormatter);
		jgen.writeString(sdf.format(value));
	}

	public String getDateFormatter() {
		return dateFormatter;
	}

	public void setDateFormatter(String dateFormatter) {
		if (dateFormatter != null) {
			DateSerializer.dateFormatter = dateFormatter;
		}
	}

}