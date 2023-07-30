package hu.bearmaster.springtutorial.boot.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import hu.bearmaster.springtutorial.boot.model.User;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

//@JsonComponent
public class UserSerializer extends JsonSerializer<User> {

    @Override
    public void serialize(User user, JsonGenerator generator, SerializerProvider serializers) throws IOException {
        generator.writeStartObject();
        generator.writeStringField("name", user.getUsername());
        generator.writeNumberField("id", user.getId());
        generator.writeEndObject();
    }
}
