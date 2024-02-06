package com.agap.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {

    private int totalPages;
    private long totalItems;
    private int currentPage;
    private boolean first;
    private boolean last;
    private int itemsPerPage;
    private int pageSize;

    private List<T> items;

    public void setPageStats(Page pg, List<T> elts) {
        first = pg.isFirst();
        last = pg.isLast();
        currentPage = pg.getNumber() + 1;
        pageSize = pg.getSize();
        totalPages = pg.getTotalPages();
        totalItems = pg.getTotalElements();
        itemsPerPage = pg.getNumberOfElements();
        items = elts;
    }
}