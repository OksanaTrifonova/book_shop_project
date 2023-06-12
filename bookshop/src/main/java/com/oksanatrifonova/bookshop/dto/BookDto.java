package com.oksanatrifonova.bookshop.dto;

import com.oksanatrifonova.bookshop.entity.Category;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class BookDto {
    private Long id;
    @NotBlank
    private String title;
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @DecimalMax(value = "1000.0", inclusive = false, message = "Price must be less than 1000")
    private BigDecimal price;
    private Category category;
    @NotNull
    private String description;
    @NotNull
    private Long imageId;
    @NotNull
    private List<Long> authorIds;
    private boolean active;
    @NotNull
    private List<String> authorNames;

    public BookDto() {
        authorIds = new ArrayList<>();
        authorNames = new ArrayList<>();
    }
}
