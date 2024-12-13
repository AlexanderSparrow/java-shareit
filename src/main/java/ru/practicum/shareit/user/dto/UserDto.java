package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private long id;

    @NotNull(message = "e-mail должен быть указан.")
    private String email;

    @NotNull(message = "Имя должно быть указано.")
    private String name;
}
