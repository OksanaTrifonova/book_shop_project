package com.oksanatrifonova.bookshop.service;

import com.oksanatrifonova.bookshop.component.Cart;
import com.oksanatrifonova.bookshop.dto.BookOrderDto;
import com.oksanatrifonova.bookshop.dto.ItemDto;
import com.oksanatrifonova.bookshop.entity.AppUser;
import com.oksanatrifonova.bookshop.entity.BookOrder;
import com.oksanatrifonova.bookshop.entity.Item;
import com.oksanatrifonova.bookshop.exception.BookValidationException;
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

    public BookOrder placeOrder(AppUser user) {
        if ((user.getAddress() == null || user.getAddress().isEmpty()) ||
                (user.getPhoneNumber() == null || user.getPhoneNumber().isEmpty())) {
            throw new BookValidationException(FILL_THE_FORM_MSG);
        }
        List<ItemDto> orderItemsDTO = cart.getItemDtoList();
        List<Item> orderItems = itemMapper.convertToEntityList(orderItemsDTO);
        BookOrder order = createOrder(user, orderItems);
        cart.removeAllItems();
        return order;
    }

    public BookOrder createOrder(AppUser user, List<Item> items) {
        BookOrder order = new BookOrder(user, items);
        BigDecimal totalAmount = cart.calculateTotalAmount();
        order.setTotalAmount(totalAmount);
        order.setOrderDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        orderRepository.save(order);
        itemRepository.saveAll(items);
        return order;
    }

    public List<BookOrderDto> getOrdersForUser(AppUser user) {
        return orderRepository.findByUser(user)
                .stream()
                .map(orderMapper::convertToDto)
                .toList();
    }

    public BookOrderDto getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(orderMapper::convertToDto)
                .orElseThrow(() -> new BookValidationException(ORDER_NOT_FOUND_MSG));
    }

}