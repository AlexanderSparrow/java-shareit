package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    List<Item> getUserItems(long userId);

    Optional<Item> getItemById(long itemId);

    Item addNewItem(long userId, Item item);

    void delete(long userId, long itemId);

    Item updateItem(Item item);

    List<Item> searchItems(String text);
}
