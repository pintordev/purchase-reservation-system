package com.pintor.purchase_reservation_system.common.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.pintor.purchase_reservation_system.common.response.ResData;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;

@Component
public class AppUtil {

    public static String responseSerialize(ResData resData) {

        try {
            StringWriter writer = new StringWriter();
            JsonGenerator gen = new JsonFactory().createGenerator(writer);

            gen.writeStartObject();

            gen.writeStringField("code", resData.getCode().toString());
            gen.writeStringField("message", resData.getMessage().toString());

            gen.writeEndObject();
            gen.close();
            writer.close();

            return writer.toString();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
