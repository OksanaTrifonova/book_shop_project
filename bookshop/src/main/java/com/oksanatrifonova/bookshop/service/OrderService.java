package com.oksanatrifonova.bookshop.service;

import com.oksanatrifonova.bookshop.component.Cart;
import com.oksanatrifonova.bookshop.dto.OrderDto;
import com.oksanatrifonova.bookshop.dto.ItemDto;
import com.oksanatrifonova.bookshop.entity.AppUser;
import com.oksanatrifonova.bookshop.entity.Order;
import com.oksanatrifonova.bookshop.entity.Item;
import com.oksanatrifonova.bookshop.exception.BookshopException;
import com.oksanatrifonova.bookshop.mapper.BookOrderMapper;
import com.oksanatrifonova.bookshop.mapper.ItemMapper;
import com.oksanatrifonova.bookshop.repository.AppOrderRepository;
import com.oksanatrifonova.bookshop.repository.ItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {
    private static final String ORDER_NOT_FOUND_MSG = "Order not found";
    private static final String FILL_THE_FORM_MSG = "Please fill in your address and phone number in your profile before placing an order.";
    private AppOrderRepository orderRepository;
    private ItemRepository itemRepository;
    private BookOrderMapper orderMapper;
    private Cart cart;
    private final ItemMapper itemMapper;

    public Order placeOrder(AppUser user) {
        if ((user.getAddress() == null || user.getAddress().isEmpty()) ||
                (user.getPhoneNumber() == null || user.getPhoneNumber().isEmpty())) {
            throw new BookshopException(FILL_THE_FORM_MSG);
        }
        List<ItemDto> orderItemsDTO = cart.getItemDtoList();
        List<Item> orderItems = itemMapper.convertToEntityList(orderItemsDTO);
        Order order = createOrder(user, orderItems);
        cart.removeAllItems();
        return order;
    }

    public Order createOrder(AppUser user, List<Item> items) {
        Order order = new Order(user, items);
        BigDecimal totalAmount = cart.calculateTotalAmount();
        order.setTotalAmount(totalAmount);
        order.setOrderDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        orderRepository.save(order);
        itemRepository.saveAll(items);
        return order;
    }

    public List<OrderDto> getOrdersForUser(AppUser user) {
        return orderRepository.findByUser(user)
                .stream()
                .map(orderMapper::convertToDto)
                .toList();
    }

    public OrderDto getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(orderMapper::convertToDto)
                .orElseThrow(() -> new BookshopException(ORDER_NOT_FOUND_MSG));
    }

}