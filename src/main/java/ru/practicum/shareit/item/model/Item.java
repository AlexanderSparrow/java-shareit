package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Item {
    private long id;
    private long ownerId;
    @NotBlank (message = "Наименование должно быть указано.")
    private String name;
    @NotBlank(message = "Предмет должен иметь описание.")
    private String description;
    private boolean available;
    private String request;
}

