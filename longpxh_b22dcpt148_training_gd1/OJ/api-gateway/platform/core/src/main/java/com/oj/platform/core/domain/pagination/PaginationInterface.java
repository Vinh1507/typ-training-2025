package com.oj.platform.core.domain.pagination;

//
public interface PaginationInterface {
    default Integer getPerPage() {
        return 10;
    }

    default Integer getPage() {
        return 1;
    }

    /**
     * Get pagination offset for database query (0-based)
     *
     * @return offset = (page - 1) * perPage
     */
    default Integer getOffset() {
        return (getPage() - 1) * getPerPage();
    }
}
