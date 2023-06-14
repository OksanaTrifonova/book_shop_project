package com.oksanatrifonova.bookshop.mapper;

import com.oksanatrifonova.bookshop.dto.ItemDto;
import com.oksanatrifonova.bookshop.entity.Item;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemMapper {
    public Item convertToEntity(ItemDto itemDTO) {
        Item item = new Item();
        item.setBook(itemDTO.getBook());
        item.setQuantity(itemDTO.getQuantity());
        item.setPrice(itemDTO.getPrice());
        return item;
    }

    public ItemDto convertToDTO(Item item) {
        ItemDto itemDTO = new ItemDto();
        itemDTO.setBook(item.getBook());
        itemDTO.setQuantity(item.getQuantity());
        itemDTO.setPrice(item.getPrice());
        return itemDTO;
    }

    public List<Item> convertToEntityList(List<ItemDto> itemDTOList) {
        return itemDTOList.stream()
                .map(this::convertToEntity)
                .toList();
    }

    public List<ItemDto> convertToDtoList(List<Item> itemList) {
        return itemList.stream()
                .map(this::convertToDTO)
                .toList();

    }
}