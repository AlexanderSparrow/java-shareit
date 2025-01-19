package ru.practicum.shareit.request;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithResponsesDto;
import ru.practicum.shareit.request.dto.ItemResponseShortDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@UtilityClass
public class ItemRequestMapper {
    public ItemRequestDto toItemRequestDto(ItemRequest request) {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(request.getId());
        dto.setDescription(request.getDescription());
        dto.setRequestorId(request.getRequestor().getId());
        dto.setCreated(request.getCreated());
        return dto;
    }

    public ItemRequestWithResponsesDto toItemRequestWithResponsesDto(ItemRequest request, List<ItemResponseShortDto> responses) {
        ItemRequestWithResponsesDto dto = new ItemRequestWithResponsesDto();
        dto.setId(request.getId());
        dto.setDescription(request.getDescription());
        dto.setCreated(request.getCreated());
        dto.setResponses(responses);
        return dto;
    }

    public ItemResponseShortDto toItemResponseShortDto(Item item) {
        ItemResponseShortDto dto = new ItemResponseShortDto();
        dto.setItemId(item.getId());
        dto.setItemName(item.getName());
        dto.setOwnerId(item.getOwnerId());
        return dto;
    }
}
