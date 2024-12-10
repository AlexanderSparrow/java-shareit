package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class InMemoryItemRepository implements ItemRepository {
    private final List<Item> items = new ArrayList<>();
    private final UserRepository userRepository;

    @Override
    public List<Item> getUserItems(long userId) {
        List<Item> userItems = items.stream()
                .filter(item -> item.getOwnerId() == userId)
                .toList();
        log.info("Список вещей пользователя: {}", userItems);
        return userItems;
    }

    @Override
    public Item getItemById(long itemId) {
        return items.stream()
                .filter(item -> item.getId() == itemId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Вещь с указанным ID не найдена."));
    }

    @Override
    public Item addNewItem(long userId, Item item) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с ID " + userId + " не найден.");
        }
        if (item.getName() == null || item.getName().isBlank()) {
            throw new IllegalArgumentException("Наименоване вещи не может быть пустым!");
        }

        if (item.getDescription() == null || item.getDescription().isBlank()) {
            throw new IllegalArgumentException("Описание вещи не может быть пустым!");
        }
        long newId = items.stream()
                .mapToLong(Item::getId)
                .max()
                .orElse(0) + 1;
        item.setId(newId);
        item.setOwnerId(userId);
        items.add(item);
        log.info("Добавлена вещь: {}", item);
        return item;
    }

    @Override
    public void delete(long userId, long itemId) {
        items.removeIf(item -> item.getId() == itemId && item.getOwnerId() == userId);
        log.info("Удалена вещь с id {} у пользоателя с id {}", itemId, userId);
    }

    @Override
    public Item partialUpdate(long userId, Map<String, Object> updates) {
        if (updates == null || !updates.containsKey("id") || updates.get("id") == null) {
            throw new IllegalArgumentException("Поле 'id' не может быть пустым!");
        }
        if (!updates.containsKey("available")) {
            throw new IllegalArgumentException("Поле 'available' обязательно для обновления!");
        }

        long itemId;
        try {
            itemId = ((Number) updates.get("id")).longValue();
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Поле 'id' должно быть числом.");
        }

        // Поиск вещи
        Item item = items.stream()
                .filter(i -> i.getId() == itemId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Вещь для обновления не найдена."));

        // Проверка владельца
        if (item.getOwnerId() != userId) {
            throw new IllegalArgumentException("Только владелец может обновлять вещь.");
        }

        // Обновление полей
        updates.forEach((key, value) -> {
            switch (key) {
                case "name" -> {
                    if (value == null || ((String) value).isBlank()) {
                        throw new IllegalArgumentException("Поле 'name' не может быть пустым!");
                    }
                    item.setName((String) value);
                }
                case "description" -> item.setDescription((String) value);
                case "available" -> {
                    if (!(value instanceof Boolean)) {
                        throw new IllegalArgumentException("Поле 'available' должно быть логическим значением.");
                    }
                    item.setAvailable((Boolean) value);
                }
                default -> log.warn("Неизвестное поле для обновления: {}", key);
            }
        });

        log.info("Обновлена (частично) вещь: {}", item);
        return item;
    }

    @Override
    public List<Item> searchItems(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }

        String textLowerCase = text.toLowerCase();
        List<Item> searchResult = items.stream()
                .filter(item -> {
                    String name = item.getName() != null ? item.getName().toLowerCase() : "";
                    String description = item.getDescription() != null ? item.getDescription().toLowerCase() : "";
                    return (name.contains(textLowerCase) || description.contains(textLowerCase)) && item.isAvailable();
                })
                .toList();
        log.info("Результат поиска: {}.", searchResult);
        return searchResult;
    }
}
