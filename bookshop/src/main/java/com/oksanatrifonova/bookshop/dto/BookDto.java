package com.oksanatrifonova.bookshop.dto;

import com.oksanatrifonova.bookshop.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class BookDto {
    private Long id;

    private String title;
    private BigDecimal price;
    private Category category;
    private String description;
    private Long imageId;
    private List<Long> authorIds;
    private boolean active;
    private List<String> authorNames;

    public BookDto() {
        authorIds = new ArrayList<>();
        authorNames = new ArrayList<>();
    }
}
