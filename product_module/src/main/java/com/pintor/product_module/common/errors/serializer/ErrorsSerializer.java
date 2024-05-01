package com.pintor.product_module.common.errors.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;

import java.io.IOException;

@JsonComponent
public class ErrorsSerializer extends JsonSerializer<Errors> {

    @Override
    public void serialize(Errors errors, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {

        gen.writeStartArray();

        errors.getFieldErrors().forEach(err -> {
            try {
                gen.writeStartObject();
                gen.writeStringField("field", err.getField());
                gen.writeStringField("code", err.getCode());
                if (err.getArguments() != null) {
                    gen.writeFieldName("value");
                    gen.writeStartArray();
                    for (Object arg : err.getArguments()) {
                        gen.writeString(arg.toString());
                    }
                    gen.writeEndArray();
                }
                if (err.getRejectedValue() != null) {
                    gen.writeStringField("value", err.getRejectedValue().toString());
                }
                gen.writeEndObject();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        errors.getGlobalErrors().forEach(err -> {
            try {
                gen.writeStartObject();
                gen.writeStringField("code", err.getCode());
                if (err.getArguments() != null) {
                    gen.writeFieldName("value");
                    gen.writeStartArray();
                    for (Object arg : err.getArguments()) {
                        gen.writeString(arg.toString());
                    }
                    gen.writeEndArray();
                }
                gen.writeEndObject();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        gen.writeEndArray();
    }
}
