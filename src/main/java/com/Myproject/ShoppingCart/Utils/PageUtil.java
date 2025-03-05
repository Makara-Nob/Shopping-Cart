package com.Myproject.ShoppingCart.Utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public interface PageUtil {
    int DEFAULT_PAGE_LIMIT = 10;
    int DEFAULT_PAGE_NUMBER = 0;
    String PAGE_LIMIT = "limit";
    String PAGE_NUMBER = "page";

    static Pageable getPageable(int pageNumber, int pageSize) {
        if (pageNumber < DEFAULT_PAGE_NUMBER) pageNumber = DEFAULT_PAGE_NUMBER;
        if (pageSize < DEFAULT_PAGE_LIMIT) pageSize = DEFAULT_PAGE_LIMIT;

        return PageRequest.of(pageNumber, pageSize);
    }
}
