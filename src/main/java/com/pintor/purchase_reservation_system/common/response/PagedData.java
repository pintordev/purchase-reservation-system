package com.pintor.purchase_reservation_system.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class PagedData<T> {

    private final List<T> list;
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

    private PagedData(Page<T> pagedList) {
        this.list = pagedList.getContent();
        this.page = pagedList.getPageable().getPageNumber() + 1;
        this.size = pagedList.getPageable().getPageSize();
        this.sort = pagedList.getSort().stream()
                .map(s -> Map.of(
                        "property", s.getProperty(),
                        "direction", s.getDirection().toString().toLowerCase()
                ))
                .collect(Collectors.toList());
        this.firstPage = 1;
        this.prevPage = pagedList.hasPrevious() ? pagedList.getPageable().getPageNumber() : null;
        this.nextPage = pagedList.hasNext() ? pagedList.getPageable().getPageNumber() + 2 : null;
        this.lastPage = pagedList.getTotalPages() == 0 ? 1 : pagedList.getTotalPages();
        this.first = pagedList.isFirst();
        this.last = pagedList.isLast();
        this.totalPages = pagedList.getTotalPages() == 0 ? 1 : pagedList.getTotalPages();
        this.totalElements = pagedList.getTotalElements();
    }

    public static <T> PagedData<T> of(Page<T> pagedList) {
        return new PagedData<>(pagedList);
    }

    public PagedData(PagedData<T> pagedData, List<T> mappedData) {
        this.list = mappedData;
        this.page = pagedData.page;
        this.size = pagedData.size;
        this.sort = pagedData.sort;
        this.firstPage = pagedData.firstPage;
        this.prevPage = pagedData.prevPage;
        this.nextPage = pagedData.nextPage;
        this.lastPage = pagedData.lastPage;
        this.first = pagedData.first;
        this.last = pagedData.last;
        this.totalPages = pagedData.totalPages;
        this.totalElements = pagedData.totalElements;
    }

    public <R> PagedData<R> map(Function<? super T, ? extends R> converter) {
        List<R> mappedData = list.stream()
                .map(converter)
                .collect(Collectors.toList());
        return new PagedData<R>((PagedData<R>) this, mappedData);
    }
}
