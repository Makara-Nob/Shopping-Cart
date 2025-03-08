package com.Myproject.ShoppingCart.Response;


import lombok.Builder;
import java.util.List;

@Builder
public record PaginationResponse<T>(
        List<T> content,
        int pageSize,
        int pageNumber,
        int totalPages,
        long totalElements,
        long numberOfElements,
        boolean first,
        boolean last,
        boolean empty
) {
}
