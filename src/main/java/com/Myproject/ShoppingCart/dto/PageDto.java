package com.Myproject.ShoppingCart.dto;

import com.Myproject.ShoppingCart.Response.PaginationResponse;
import com.Myproject.ShoppingCart.Utils.PageUtil;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.Optional;

@Data
public class PageDto<T> {
    private final PaginationResponse<T> pagination;

    public PageDto(Page<T> page) {
        this.pagination = new PaginationResponse<>(
                page.getContent(),
                page.getPageable().getPageSize(),
                page.getPageable().getPageNumber(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getNumberOfElements(),
                page.isFirst(),
                page.isLast(),
                page.isEmpty()
        );
    }

    /**
     * Builds a Pageable object from request parameters.
     * Ensures safe parsing and uses default values if parameters are missing or invalid.
     *
     * @param params request parameters containing pagination info
     * @return Pageable object
     */
    public static Pageable buildPageable(Map<String, String> params) {
        int pageLimit = parseOrDefault(params.get(PageUtil.PAGE_LIMIT), PageUtil.DEFAULT_PAGE_LIMIT);
        int pageNumber = parseOrDefault(params.get(PageUtil.PAGE_NUMBER), PageUtil.DEFAULT_PAGE_NUMBER);
        return PageUtil.getPageable(pageNumber, pageLimit);
    }

    /**
     * Safely parses an integer from a string, returning a default value on failure.
     *
     * @param value        the string value to parse
     * @param defaultValue the fallback value if parsing fails
     * @return parsed integer or default value
     */
    private static int parseOrDefault(String value, int defaultValue) {
        return Optional.ofNullable(value)
                .map(v -> {
                    try {
                        return Integer.parseInt(v);
                    } catch (NumberFormatException e) {
                        return defaultValue;
                    }
                })
                .orElse(defaultValue);
    }
}