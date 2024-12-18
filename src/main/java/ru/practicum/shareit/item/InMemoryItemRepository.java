package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class InMemoryItemRepository implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private long nextId = 1;

    @Override
    public Item updateItem(Item item) {
        if (!items.containsKey(item.getId())) {
            throw new NotFoundException("Вещь с id: " + item.getId() + " не найдена.");
        }
        items.put(item.getId(), item); // Перезаписываем элемент с тем же id
        return item;
    }

    @Override
    public List<Item> getUserItems(long userId) {
        return items.values().stream()
                .filter(item -> item.getOwnerId() == userId)
                .toList();
    }

    @Override
    public Item getItemById(long itemId) {
        return items.get(itemId);
    }

    @Override
    public Item addNewItem(long userId, Item item) {
        item.setId(nextId++);
        item.setOwnerId(userId);
        items.put(item.getId(), item);
        log.info("Добавлена вещь: {}", item);
        return item;
    }

    @Override
    public void delete(long userId, long itemId) {
        Item item = items.get(itemId);
        if (item == null || item.getOwnerId() != userId) {
            throw new IllegalArgumentException("Вещь не найдена или не принадлежит пользователю.");
        }
        items.remove(itemId);
        log.info("Удалена вещь с id: {}", itemId);
    }

    @Override
    public List<Item> searchItems(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }

        String lowerText = text.toLowerCase();

        List<Item> searchResult = items.values().stream()
                .filter(item -> (item.getName().toLowerCase().contains(lowerText)
                        || item.getDescription().toLowerCase().contains(lowerText))
                        && Boolean.TRUE.equals(item.getAvailable()))
                .toList();
        log.info("Результат поиска: {}.", searchResult);
        return searchResult;
    }
}
