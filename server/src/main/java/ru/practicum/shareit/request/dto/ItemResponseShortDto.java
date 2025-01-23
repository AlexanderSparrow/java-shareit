package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ItemResponseShortDto {
    private Long itemId;
    private String name;
    private Long ownerId;
}

