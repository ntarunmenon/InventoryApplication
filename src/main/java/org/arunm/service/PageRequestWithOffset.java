package org.arunm.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageRequestWithOffset implements Pageable {

    private int limit;
    private int offset;

    public PageRequestWithOffset(int limit, int offset) {
        this.limit = limit;
        this.offset = offset;
    }

    @Override
    public int getPageNumber() {
        return 1;
    }

    @Override
    public int getPageSize() {
        return limit > 0 ? limit : Integer.MAX_VALUE;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public Sort getSort() {
        return Sort.unsorted();
    }

    @Override
    public Pageable next() {
        return null;
    }

    @Override
    public Pageable previousOrFirst() {
        return this;
    }

    @Override
    public Pageable first() {
        return this;
    }

    @Override
    public boolean hasPrevious() {
        return false;
    }
}