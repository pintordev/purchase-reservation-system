package com.pintor.purchase_module.common.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.pintor.purchase_module.common.response.ResData;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    public static String toCamelCase(String property) {
        String[] bits = property.split("_");
        return IntStream.range(0, bits.length)
                .mapToObj(i -> i != 0 ? bits[i].substring(0, 1).toUpperCase() + bits[i].substring(1) : bits[i])
                .collect(Collectors.joining());
    }
}
