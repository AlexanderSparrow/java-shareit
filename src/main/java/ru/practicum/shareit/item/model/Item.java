package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;

@Data
public class Item {
    private long id;
    private long ownerId;
    @NotBlank (message = "Наименование должно быть указано.")
    private String name;
    @NotBlank(message = "Предмет должен иметь описание.")
    private String description;
    private Boolean available;
    private ItemRequest request;

}


