package com.cim.utils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CustomDateTimeSerializer extends JsonSerializer<LocalDateTime> {
	
	private static DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

	@Override
	public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializerProvider)
			throws IOException, JsonProcessingException {
		gen.writeString(formatter.format(value));
		
	}

}
