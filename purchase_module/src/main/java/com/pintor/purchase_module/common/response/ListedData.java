package com.pintor.purchase_module.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class ListedData<R> {

    private final List<R> list;

    public <T> ListedData(List<T> data, Function<T, R> converter) {
        if (data.isEmpty()) {
            this.list = new ArrayList<>();
            return;
        }
        this.list = data.stream()
                .map(converter)
                .collect(Collectors.toList());
    }

    public static <T, R> ListedData<R> of(List<T> data, Function<T, R> converter) {
        return new ListedData(data, converter);
    }
}
