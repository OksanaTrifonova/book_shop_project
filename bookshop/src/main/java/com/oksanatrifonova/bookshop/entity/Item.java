package com.oksanatrifonova.bookshop.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne(fetch = FetchType.EAGER)
    private Book book;
    private int quantity;
    private BigDecimal price;
    public Item(Book book, int quantity,BigDecimal price) {
        this.book = book;
        this.quantity = quantity;
        this.price=price;
    }

    public Item() {
    }

}