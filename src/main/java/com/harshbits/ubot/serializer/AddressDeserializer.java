package com.harshbits.ubot.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.harshbits.ubot.domain.support.Address;

public class AddressDeserializer extends JsonDeserializer<Address> {

	@Override
	public Address deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		try {
			return jp.readValueAs(Address.class);
		} catch (Exception e) {
			Address address = new Address();
			address.setAddress(jp.readValueAs(String.class));
			return address;
		}
	}

}
