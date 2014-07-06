package grouptabs.backend.deprecated;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class MoneySerializer extends JsonSerializer<Map<String, Long>> {

	@Override
	public void serialize(Map<String, Long> map, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		
		jgen.writeStartObject();
		
		for (Map.Entry<String, Long> entry : map.entrySet()) {
			jgen.writeFieldName(entry.getKey());
			jgen.writeString(entry.getValue() / 100 + "." + entry.getValue() % 100);
		}
		
		jgen.writeEndObject();
	}
}
