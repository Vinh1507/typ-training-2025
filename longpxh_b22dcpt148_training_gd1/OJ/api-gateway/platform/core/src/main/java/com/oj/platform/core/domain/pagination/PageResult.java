package com.oj.platform.core.domain.pagination;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PageResult<T> {
    private final List<T> items;
    private final Long total;
    private final Integer page;
    private final Integer perPage;

    public PageResult(List<T> items, PaginationInterface pagination, Long total) {
        this.items = items;
        this.total = total;
        this.page = pagination.getPage();
        this.perPage = pagination.getPerPage();
    }

    public static <T> PageResult<T> of(List<T> items, int total, int page, int perPage) {
        return new PageResult<>(items, new Pagination(perPage, page), (long) total);
    }

    public List<T> getItems() {
        return items;
    }

    public Long getTotal() {
        return total;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getPerPage() {
        return perPage;
    }

    public Long getTotalPages() {
        return (long) Math.ceil((double) total / perPage);
    }

    /**
     * Map each item to another type while preserving pagination metadata.
     */
    public <R> PageResult<R> map(Function<? super T, ? extends R> mapper) {
        List<R> mapped = items == null ? List.of() : items.stream().map(mapper).collect(Collectors.toList());
        return new PageResult<>(mapped, new Pagination(perPage, page), total);
    }

    /**
     * Map the whole list at once (useful if you already have a collection mapper).
     */
    public <R> PageResult<R> mapAll(Function<? super List<T>, ? extends List<R>> mapper) {
        List<R> mapped = mapper.apply(items == null ? List.of() : items);
        return new PageResult<>(mapped, new Pagination(perPage, page), total);
    }
}
