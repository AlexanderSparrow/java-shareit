package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Map;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    ItemService itemService;

    @GetMapping
    public List<Item> getUserItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получен запрос на список вещей пользователя с id: {}.", userId);
        return itemService.getUserItems(userId);
    }

    @GetMapping("/{itemId}")
    public Item getItemById(@PathVariable long itemId) {
        log.info("Получен запрос на просмотр вещи с id: {}.", itemId);
        return itemService.getItemById(itemId);
    }

    @PostMapping
    public Item addNewItem(@Valid
                           @RequestHeader("X-Sharer-User-Id") long userId,
                           @RequestBody Item item) {
        log.info("Получен запрос на добавление новой вещи пользователем с id: {}.", userId);
        return itemService.addNewItem(userId, item);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") long userId,
                           @PathVariable(name = "itemId") long itemId) {
        log.info("Получен запрос на удаление вещи с id: {} у пользователя с id: {}.", itemId, userId);
        itemService.delete(userId, itemId);
    }

    @PatchMapping("/{itemId}")
    public Item updateItem(@Valid
                           @RequestHeader("X-Sharer-User-Id") long userId,
                           @PathVariable(name = "itemId") long itemId,
                           @RequestBody Map<String, Object> updates) {
        log.info("Получен запрос на обновление информации о вещи с id: {}.", itemId);
        return itemService.partialUpdate(userId, itemId, updates); // Частичное обновление
    }

    @GetMapping("/search")
    public List<Item> searchItems(@RequestParam String text) {
        log.info("Получен запрос на поиск вещей по строке: {}.", text);
        return itemService.searchItems(text);
    }

}
