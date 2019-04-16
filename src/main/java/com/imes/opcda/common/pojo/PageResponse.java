package com.imes.opcda.common.pojo;

import lombok.Data;

import java.util.List;

@Data
public class PageResponse<T> {
    private long totalCount;
    private List<T>  items;

    public PageResponse(long totalCount, List<T> items) {
        this.totalCount = totalCount;
        this.items = items;
    }
}
