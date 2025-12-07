package com.oj.platform.core.domain.pagination;

public class Pagination implements PaginationInterface {
    private final Integer perPage;
    private final Integer page;

    public Pagination(Integer perPage, Integer page) {
        this.perPage = perPage;
        this.page = page;
    }

    @Override
    public Integer getPerPage() {
        return perPage;
    }

    @Override
    public Integer getPage() {
        return page;
    }

}
