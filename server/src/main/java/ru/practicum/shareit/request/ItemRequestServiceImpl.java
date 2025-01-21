package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithResponsesDto;
import ru.practicum.shareit.request.dto.ItemResponseShortDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequestDto createNewRequest(Long userId, ItemRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не найден."));

        ItemRequest request = new ItemRequest();
        request.setDescription(requestDto.getDescription());
        request.setRequestor(user);
        request.setCreated(Instant.now());

        requestRepository.save(request);
        requestDto.setId(request.getId());
        requestDto.setRequestorId(userId);
        requestDto.setCreated(request.getCreated());
        return requestDto;
    }

    @Override
    public List<ItemRequestWithResponsesDto> findAllRequestsByUserWithResponses(Long userId) {
        List<ItemRequest> requests = requestRepository.findByRequestorIdOrderByCreatedDesc(userId);
        return requests.stream()
                .map(request -> ItemRequestMapper.toItemRequestWithResponsesDto(request,
                        itemRepository.findAllByRequestId(request.getId())
                                .stream()
                                .map(ItemRequestMapper::toItemResponseShortDto)
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestWithResponsesDto> getAllRequests(Long userId) {
        List<ItemRequest> requests = requestRepository.findAllByOtherUsers(userId);
        return requests.stream()
                .map(request -> ItemRequestMapper.toItemRequestWithResponsesDto(request,
                        itemRepository.findAllByRequestId(request.getId())
                                .stream()
                                .map(ItemRequestMapper::toItemResponseShortDto)
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestWithResponsesDto getRequestById(Long userId, Long requestId) {
        ItemRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос с id: " + requestId + " не найден."));

        List<ItemResponseShortDto> responses = itemRepository.findAllByRequestId(requestId).stream()
                .map(ItemRequestMapper::toItemResponseShortDto)
                .collect(Collectors.toList());

        return ItemRequestMapper.toItemRequestWithResponsesDto(request, responses);
    }
}
