package ru.practicum.shareit.item.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ItemByRequestResponseDto {
    private long id;
    private String name;
    private long ownerId;
}