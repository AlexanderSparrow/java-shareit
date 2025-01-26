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
        if (request == null) {
            return null;
        }

        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(request.getId());
        dto.setDescription(request.getDescription());
        dto.setRequestorId(request.getRequestor() != null ? request.getRequestor().getId() : null);
        dto.setCreated(request.getCreated());
        return dto;
    }

    public ItemRequestWithResponsesDto toItemRequestWithResponsesDto(ItemRequest request, List<ItemResponseShortDto> responses) {
        if (request == null) {
            return null;
        }
        ItemRequestWithResponsesDto dto = new ItemRequestWithResponsesDto();
        dto.setId(request.getId());
        dto.setDescription(request.getDescription());
        dto.setCreated(request.getCreated());
        dto.setItems(responses);
        return dto;
    }

    public ItemResponseShortDto toItemResponseShortDto(Item item) {
        if (item == null) {
            return null;
        }
        ItemResponseShortDto dto = new ItemResponseShortDto();
        dto.setItemId(item.getId());
        dto.setName(item.getName());
        dto.setOwnerId(item.getOwnerId());
        return dto;
    }
}
