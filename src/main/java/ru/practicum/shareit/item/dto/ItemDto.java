package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private long id;

    private long ownerId;

    @NotBlank(message = "Наименование должно быть указано.")
    private String name;

    @NotBlank(message = "Описание должно быть указано.")
    private String description;

    @NotNull(message = "Доступность должно быть указана.")
    private Boolean available;

    private Long requestId;
}