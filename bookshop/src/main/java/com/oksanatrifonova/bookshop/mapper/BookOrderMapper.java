package com.oksanatrifonova.bookshop.mapper;

import com.oksanatrifonova.bookshop.dto.OrderDto;
import com.oksanatrifonova.bookshop.entity.Order;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class BookOrderMapper {
    private final ItemMapper itemMapper;

    public OrderDto convertToDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setItems(itemMapper.convertToDtoList(order.getItems()));
        orderDto.setTotalAmount(order.getTotalAmount());
        orderDto.setOrderDateTime(order.getOrderDateTime());
        return orderDto;
    }


}
