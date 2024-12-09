package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository{

    @Override
    public List<Item> getUserItems(long userId) {
        return List.of();
    }

    @Override
    public Item getItemById(long itemId) {
        return null;
    }

    @Override
    public Item addNewItem(long userId, Item item) {
        return null;
    }

    @Override
    public void delete(long userId, long itemId) {

    }

    @Override
    public Item partialUpdate(long userId, Map<String, Object> updates) {
        return null;
    }

    @Override
    public List<Item> searchItems(String text) {
        return List.of();
    }
}
