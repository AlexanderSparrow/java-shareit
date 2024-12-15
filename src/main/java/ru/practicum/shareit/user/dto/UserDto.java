package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private long id;

    @Email(message = "Некорректный формат электронной почты!")
    @NotBlank(message = "e-mail должен быть указан.")
    private String email;

    @NotBlank(message = "Имя должно быть указано.")
    private String name;
}
