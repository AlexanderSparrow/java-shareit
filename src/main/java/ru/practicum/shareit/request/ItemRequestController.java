package ru.practicum.shareit.request;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithResponsesDto;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Requests", description = "Управление запросами на добавление новой вещи")
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService requestService;

    @PostMapping
    public ItemRequestDto createNewRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @RequestBody ItemRequestDto requestDto) {
        log.info("Получен запрос на создание новой вещи.");
        return requestService.createNewRequest(userId, requestDto);
    }

    @GetMapping
    public List<ItemRequestWithResponsesDto> findAllRequestsByUserWithResponses(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос на получение всех запросов на создание новой вещи пользователя с id: {}.", userId);
        return requestService.findAllRequestsByUserWithResponses(userId);
    }

    @GetMapping("/all")
    private List<ItemRequestWithResponsesDto> getAllRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос на получение всех запросов пользователей на создание новой вещи.");
        return requestService.getAllRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestWithResponsesDto getRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                      @PathVariable Long requestId) {
        log.info("Запрошена информации о запросе на создание новой вещи с id: {}.", requestId);
        return requestService.getRequestById(userId, requestId);

    }
}
