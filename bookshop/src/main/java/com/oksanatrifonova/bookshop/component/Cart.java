package com.oksanatrifonova.bookshop.component;

import com.oksanatrifonova.bookshop.dto.BookDto;
import com.oksanatrifonova.bookshop.dto.ItemDto;
import com.oksanatrifonova.bookshop.entity.Book;
import com.oksanatrifonova.bookshop.mapper.BookMapper;
import com.oksanatrifonova.bookshop.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@AllArgsConstructor
public class Cart {
    private final BookMapper bookMapper;
    private final BookService bookService;
    private final List<ItemDto> itemDtoList;

    public List<ItemDto> getItemDtoList() {
        return itemDtoList;
    }
    public void removeAllItems() {
        itemDtoList.clear();
    }
    public void removeItemById(Long id) {
        itemDtoList.removeIf(item -> Objects.equals(item.getBook().getId(), id));
    }
    public void addItemToCart(BookDto bookDto, int quantity, BigDecimal price) {
        Optional<ItemDto> existingItem = itemDtoList.stream()
                .filter(item -> item.getBook().getId().equals(bookDto.getId()))
                .findFirst();
        if (existingItem.isPresent()) {
            ItemDto item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            Book book = bookMapper.toEntity(bookDto);
            itemDtoList.add(new ItemDto(book, quantity, price));
        }
    }
    public void addToCart(Long bookId) {
        BookDto book = bookService.getBookById(bookId);
        if (book != null) {
            addItemToCart(book, 1, book.getPrice());
            calculateTotalAmount();
        }
    }
    public void update(Long id, int quantity) {
        Optional<ItemDto> optionalItem = itemDtoList.stream()
                .filter(item -> item.getBook().getId().equals(id))
                .findFirst();
        optionalItem.ifPresent(item -> item.setQuantity(quantity));
    }
    public BigDecimal calculateTotalAmount() {
        return itemDtoList.stream()
                .map(item -> item.getBook().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    public int getItemCount() {
        return itemDtoList.stream()
                .mapToInt(ItemDto::getQuantity)
                .sum();
    }
}
