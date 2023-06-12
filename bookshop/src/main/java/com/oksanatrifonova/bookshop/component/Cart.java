package com.oksanatrifonova.bookshop.component;

import com.oksanatrifonova.bookshop.dto.BookDto;
import com.oksanatrifonova.bookshop.dto.ItemDto;
import com.oksanatrifonova.bookshop.entity.Book;
import com.oksanatrifonova.bookshop.mapper.BookMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component

public class Cart {
    private final BookMapper bookMapper;
    private final List<ItemDto> cart;
    public Cart(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
        this.cart = new ArrayList<>();
    }

    public List<ItemDto> getCart() {
        return cart;
    }
    public void removeAllItems() {
        cart.clear();
    }
    public void removeItemById(Long id) {
        cart.removeIf(item -> Objects.equals(item.getBook().getId(), id));
    }
public void addItemToCart(BookDto bookDto, int quantity, BigDecimal price){
    Optional<ItemDto> existingItem = cart.stream()
            .filter(item -> item.getBook().getId().equals(bookDto.getId()))
            .findFirst();
    if (existingItem.isPresent()){
        ItemDto item = existingItem.get();
        item.setQuantity(item.getQuantity()+quantity);
    } else {
        Book book = bookMapper.toEntity(bookDto);
        cart.add(new ItemDto(book, quantity, price));
    }
}
    public void update(Long id,int quantity){
        Optional<ItemDto> optionalItem =cart.stream()
                .filter(item -> item.getBook().getId().equals(id))
                .findFirst();
        optionalItem.ifPresent(item -> item.setQuantity(quantity));
    }

    public BigDecimal calculateTotalAmount() {
        BigDecimal totalAmount =BigDecimal.ZERO;
        for (ItemDto item : cart) {
            BigDecimal itemPrice = item.getBook().getPrice();
            int itemQuantity = item.getQuantity();
            BigDecimal itemTotal = itemPrice.multiply(BigDecimal.valueOf(itemQuantity));
            totalAmount = totalAmount.add(itemTotal);
        }
        return totalAmount;
    }

}
