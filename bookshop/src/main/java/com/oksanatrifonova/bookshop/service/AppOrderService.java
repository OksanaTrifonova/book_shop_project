package com.oksanatrifonova.bookshop.service;

import com.oksanatrifonova.bookshop.component.Cart;
import com.oksanatrifonova.bookshop.dto.BookOrderDto;
import com.oksanatrifonova.bookshop.entity.AppUser;
import com.oksanatrifonova.bookshop.entity.BookOrder;
import com.oksanatrifonova.bookshop.entity.Item;
import com.oksanatrifonova.bookshop.mapper.BookOrderMapper;
import com.oksanatrifonova.bookshop.repository.AppOrderRepository;
import com.oksanatrifonova.bookshop.repository.ItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AppOrderService {
    private AppOrderRepository orderRepository;
    private ItemRepository itemRepository;
    private BookOrderMapper orderMapper;

    public BookOrder createOrder(AppUser user, List<Item> items, Cart cart) {
        BookOrder order = new BookOrder(user, items);
        BigDecimal totalAmount = cart.calculateTotalAmount();
        order.setTotalAmount(totalAmount);
        order.setOrderDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        orderRepository.save(order);

        for (Item item : items) {
            itemRepository.save(item);
        }

        return order;
    }

    public List<BookOrderDto> getOrdersForUser(AppUser user) {
        return orderRepository.findByUser(user)
                .stream()
                .map(orderMapper::convertToDto)
                .collect(Collectors.toList());
    }


    public BookOrderDto getOrderById(Long id) {
        BookOrder order = orderRepository.findById(id).orElseThrow(null);
        return orderMapper.convertToDto(order);
    }
}