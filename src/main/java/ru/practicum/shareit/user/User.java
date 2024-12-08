package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class User {
    private long id;

    @Email(message = "Некорректный формат электронной почты!")
    @NotBlank(message = "Электронная почта не может быть пустой!")
    private String email;

    @NotBlank(message = "Необходимо указать имя пользователя!")
    private String name;

}
