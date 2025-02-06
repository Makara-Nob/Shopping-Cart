package com.Myproject.ShoppingCart.Pagination;

import com.Myproject.ShoppingCart.dto.ProductDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class ProductResponse {

    private List<ProductDto> content;

    private int pageNo;

    private int pageSize;

    private long totalElements;

    private int totalPages;

    private boolean last;
}
