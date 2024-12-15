package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;
import java.util.Map;

public interface ItemService {
    List<ItemDto> getUserItems(long userId);

    ItemDto getItemById(long itemId);

    ItemDto addNewItem(@Valid long userId, ItemDto itemDto);

    void delete(long userId, long itemId);

    ItemDto partialUpdate(@Valid long userId, long itemId, Map<String, Object> updates);

    List<ItemDto> searchItems(String text);
}
