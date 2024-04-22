package com.pintor.purchase_reservation_system.common.response;

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

    private ListedData(List<R> list) {
        this.list = list;
    }

    public static <T, R> ListedData<R> of(List<T> data, Function<T, R> converter) {
        if (data.isEmpty()) {
            return new ListedData<>(new ArrayList<>());
        }
        List<R> mappedData = data.stream()
                .map(converter)
                .collect(Collectors.toList());
        return new ListedData(mappedData);
    }
}
