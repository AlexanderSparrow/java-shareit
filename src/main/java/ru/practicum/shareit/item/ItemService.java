package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    List<ItemDto> getUserItems(long userId);

    ItemDto getItemById(long itemId);

    ItemDto addNewItem(@Valid long userId, ItemDto itemDto);

    void delete(long userId, long itemId);

    ItemDto updateItem(@Valid long userId, long itemId, ItemDto itemDto);

    List<ItemDto> searchItems(String text);
}
