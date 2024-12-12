package ru.practicum.shareit.item;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InMemoryItemService implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    public List<Item> getUserItems(long userId) {
        return itemRepository.getUserItems(userId);
    }

    @Override
    public Item getItemById(long itemId) {
        if (itemRepository.getItemById(itemId) == null)
            throw new NotFoundException("Вещь с id:" + itemId + " не найдена!");
        return itemRepository.getItemById(itemId);
    }

    @Override
    public Item addNewItem(long userId, Item item) {
        return itemRepository.addNewItem(userId, item);
    }

    @Override
    public void delete(long userId, long itemId) {
        itemRepository.delete(userId, itemId);

    }

    @Override
    public Item partialUpdate(long userId, long itemId, Map<String, Object> updates) {
        return itemRepository.partialUpdate(userId, itemId, updates);
    }

    @Override
    public List<Item> searchItems(String text) {
        return itemRepository.searchItems(text);
    }
}
