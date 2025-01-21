package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithResponsesDto;

import java.util.List;


public interface ItemRequestService {
    ItemRequestDto createNewRequest(Long userId, ItemRequestDto requestDto);

    List<ItemRequestWithResponsesDto> findAllRequestsByUserWithResponses(Long userId);

    List<ItemRequestWithResponsesDto> getAllRequests(Long userId);

    ItemRequestWithResponsesDto getRequestById(Long userId, Long requestId);
}
