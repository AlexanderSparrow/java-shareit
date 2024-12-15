package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Item {
    private long id;
    private long ownerId;
    @NotBlank(message = "Наименование должно быть указано.")
    private String name;
    @NotBlank(message = "Предмет должен иметь описание.")
    private String description;
    private Boolean available;
    private long requestId;

}


