package ru.practicum.shareit.item;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@Slf4j
@Tag(name = "Items", description = "Управление вещами")
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<ItemDto> getUserItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получен запрос на список вещей пользователя с id: {}.", userId);
        return itemService.getUserItems(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable long itemId) {
        log.info("Получен запрос на просмотр вещи с id: {}.", itemId);
        return itemService.getItemById(itemId);
    }

    @PostMapping
    public ItemDto addNewItem(@RequestHeader("X-Sharer-User-Id") long userId,
                              @Valid @RequestBody ItemDto itemDto) {
        log.info("Получен запрос на добавление новой вещи {} пользователем с id: {}.", itemDto, userId);
        return itemService.addNewItem(userId, itemDto);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") long userId,
                           @PathVariable(name = "itemId") long itemId) {
        log.info("Получен запрос на удаление вещи с id: {} у пользователя с id: {}.", itemId, userId);
        itemService.delete(userId, itemId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                              @PathVariable(name = "itemId") long itemId,
                              @RequestBody ItemDto itemDto) {
        log.info("Получен запрос на обновление информации о вещи с id: {}.", itemId);
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        log.info("Получен запрос на поиск вещей по строке: {}.", text);
        return itemService.searchItems(text);
    }
}
