package ru.practicum.shareit.request.dto;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class ItemRequestWithResponsesDto {
    private Long id;
    private String description;
    private Instant created;
    private List<ItemResponseShortDto> items;
}

