package novah.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.function.Function;

public class NovahSerializer extends JsonSerializer<Object> {

    private Function<JsonGenerator, Function<Object, Object>> serializer;

    public void setSerializer(Function<JsonGenerator, Function<Object, Object>> serializer) {
        this.serializer = serializer;
    }

    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        serializer.apply(jsonGenerator).apply(o);
    }
}
