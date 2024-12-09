package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Map;

public interface ItemService {
    List<Item> getUserItems(long userId);

    Item getItemById(long itemId);

    Item addNewItem(@Valid long userId, Item item);

    void delete(long userId, long itemId);

    Item partialUpdate(@Valid long userId, long itemId, Map<String, Object> updates);

    List<Item> searchItems(String text);
}
