package ru.practicum.shareit.request;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Requests", description = "Управление запросами на добавление новой вещи")
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final RequestClient requestClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createNewRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @RequestBody ItemRequestDto requestDto) {
        log.info("Получен запрос на создание новой вещи от пользователя с id: {}.", userId);
        return requestClient.createNewRequest(userId, requestDto);
    }

    @GetMapping
    public ResponseEntity<Object> findAllRequestsByUserWithResponses(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос на получение всех запросов на создание новой вещи пользователя с id: {}.", userId);
        return requestClient.findAllRequestsByUserWithResponses(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос пользователя с id: {} на получение всех запросов пользователей на создание новой вещи.", userId);
        return requestClient.getAllRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @PathVariable Long requestId) {
        log.info("Получен запрос на получение информации о запросе с id: {} от пользователя с id: {}.", requestId, userId);
        return requestClient.getRequestById(userId, requestId);

    }
}
