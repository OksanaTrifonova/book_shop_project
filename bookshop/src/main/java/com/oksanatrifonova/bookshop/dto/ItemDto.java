package com.oksanatrifonova.bookshop.dto;

import com.oksanatrifonova.bookshop.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class ItemDto {
    private Long id;
    private Book book;
    private int quantity;
    private BigDecimal price;

    public ItemDto() {

    }

    public ItemDto(Book book, int quantity, BigDecimal price) {
        this.book = book;
        this.quantity = quantity;
        this.price = price;
    }
}
