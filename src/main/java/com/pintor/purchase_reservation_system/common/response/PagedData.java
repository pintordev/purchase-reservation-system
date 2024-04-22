package com.pintor.purchase_reservation_system.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pintor.purchase_reservation_system.common.util.AppUtil;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
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
                        "property", AppUtil.toCamelCase(s.getProperty()),
                        "direction", s.getDirection().toString().toLowerCase()
                ))
                .collect(Collectors.toList());
        this.firstPage = 1;
        this.prevPage = pagedList.hasPrevious() ? pagedList.getPageable().getPageNumber() : null;
        this.nextPage = pagedList.hasNext() ? pagedList.getPageable().getPageNumber() + 2 : null;
        this.lastPage = pagedList.getTotalPages();
        this.first = pagedList.isFirst();
        this.last = pagedList.isLast();
        this.totalPages = pagedList.getTotalPages();
        this.totalElements = pagedList.getTotalElements();
    }

    public static <T> PagedData<T> of(Page<T> pagedList) {
        return new PagedData<>(pagedList);
    }
}
