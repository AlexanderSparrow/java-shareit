package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private long id;

    @NotBlank(message = "Наименование должно быть указано.")
    private String name;

    @NotBlank(message = "Описание должно быть указано.")
    private String description;

    @NotBlank(message = "Доступность должно быть указана.")
    private Boolean available;

    private long requestId;
}

