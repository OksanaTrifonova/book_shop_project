package com.oksanatrifonova.bookshop.mapper;

import com.oksanatrifonova.bookshop.dto.BookOrderDto;
import com.oksanatrifonova.bookshop.entity.BookOrder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class BookOrderMapper {
    private final ItemMapper itemMapper;

    public BookOrderDto convertToDto(BookOrder order) {
        BookOrderDto orderDto = new BookOrderDto();
        orderDto.setId(order.getId());
        orderDto.setItems(itemMapper.convertToDtoList(order.getItems()));
        orderDto.setTotalAmount(order.getTotalAmount());
        orderDto.setOrderDateTime(order.getOrderDateTime());
        return orderDto;
    }


}
