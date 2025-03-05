package com.Myproject.ShoppingCart.Pagination;

import java.util.List;

public record PaginationResponse<T> (
         List<T> content,
         int pageNo,
         int pageSize,
         long totalElements,
         int totalPages,
         boolean last
){}
