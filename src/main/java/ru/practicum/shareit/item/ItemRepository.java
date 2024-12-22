package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    List<Item> getUserItems(long userId);

    Item getItemById(long itemId);

    Item addNewItem(long userId, Item item);

    void delete(long userId, long itemId);

    Item updateItem(Item item);

    List<Item> searchItems(String text);
}
