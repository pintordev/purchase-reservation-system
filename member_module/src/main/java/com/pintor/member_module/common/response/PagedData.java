package com.pintor.member_module.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class PagedData<R> {

    private final List<R> list;
    private final Integer page;
    private final Integer size;
    private final List<Map<String, String>> sort;
    private final Integer firstPage;
    private final Integer prevPage;
    private final Integer nextPage;
    private final Integer lastPage;
    private final boolean first;
    private final boolean last;
    private final Integer totalPages;
    private final Long totalElements;

    public <T> PagedData(Page<T> data, Function<T, R> converter) {
        this.list = data.getContent().stream()
                .map(converter)
                .collect(Collectors.toList());
        this.page = data.getPageable().getPageNumber() + 1;
        this.size = data.getPageable().getPageSize();
        this.sort = data.getSort().stream()
                .map(s -> Map.of(
                        "property", s.getProperty(),
                        "direction", s.getDirection().toString().toLowerCase()
                ))
                .collect(Collectors.toList());
        this.firstPage = 1;
        this.prevPage = data.hasPrevious() ? data.getPageable().getPageNumber() : null;
        this.nextPage = data.hasNext() ? data.getPageable().getPageNumber() + 2 : null;
        this.lastPage = data.getTotalPages() == 0 ? 1 : data.getTotalPages();
        this.first = data.isFirst();
        this.last = data.isLast();
        this.totalPages = data.getTotalPages() == 0 ? 1 : data.getTotalPages();
        this.totalElements = data.getTotalElements();
    }

    public static <T, R> PagedData<R> of(Page<T> data, Function<T, R> converter) {
        return new PagedData(data, converter);
    }
}
