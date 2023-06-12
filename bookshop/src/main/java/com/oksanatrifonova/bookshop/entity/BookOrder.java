package com.oksanatrifonova.bookshop.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@Setter
@Getter
@Table(name = "book_order")
public class BookOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    private AppUser user;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "order_item",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Item> items;
    private BigDecimal totalAmount;
    private LocalDateTime orderDateTime;

    public BookOrder() {
        this.orderDateTime = LocalDateTime.now();
    }

    public BookOrder(AppUser user, List<Item> items) {
        this.user = user;
        this.items = items;
    }


}
