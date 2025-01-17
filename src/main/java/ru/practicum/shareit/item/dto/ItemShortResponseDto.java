package ru.practicum.shareit.item.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ItemShortResponseDto {
    private long id;
    private String name;
}
