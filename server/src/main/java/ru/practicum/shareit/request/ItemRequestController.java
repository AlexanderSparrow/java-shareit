package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithResponsesDto;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto createNewRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @RequestBody ItemRequestDto requestDto) {
        log.info("Получен запрос на создание новой вещи от пользователя с id: {}.", userId);
        return requestService.createNewRequest(userId, requestDto);
    }

    @GetMapping
    public List<ItemRequestWithResponsesDto> findAllRequestsByUserWithResponses(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос на получение всех запросов на создание новой вещи пользователя с id: {}.", userId);
        return requestService.findAllRequestsByUserWithResponses(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestWithResponsesDto> getAllRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос пользователя с id: {} на получение всех запросов пользователей на создание новой вещи.", userId);
        return requestService.getAllRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestWithResponsesDto getRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                      @PathVariable Long requestId) {
        log.info("Получен запрос на получение информации о запросе с id: {} от пользователя с id: {}.", requestId, userId);
        return requestService.getRequestById(userId, requestId);

    }
}
