package com.cim.utils;

import java.io.IOException;
import java.time.LocalDateTime;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class CustomDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {



	@Override
	public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext ctx)
			throws IOException, JsonProcessingException {
		String date = jsonParser.getText();
		try {
			return LocalDateTime.parse(date);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
