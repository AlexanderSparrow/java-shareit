package ru.practicum.shareit.item;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemResponseDto;

@UtilityClass
public class ItemResponseMapper {
    public ItemResponseDto toItemResponseDto(Long itemId, String itemName) {
        return new ItemResponseDto(itemId, itemName);
    }
}
