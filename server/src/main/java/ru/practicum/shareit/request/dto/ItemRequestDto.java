package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ItemRequestDto {
    private Long id;
    private String description;
    private Long requestorId;
    private Instant created;
}
