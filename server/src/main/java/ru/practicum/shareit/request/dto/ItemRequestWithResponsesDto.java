package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestWithResponsesDto {
    private Long id;
    private String description;
    private Instant created;
    private List<ItemResponseShortDto> items;
}

