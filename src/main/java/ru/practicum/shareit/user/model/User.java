package ru.practicum.shareit.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email(message = "Некорректный формат электронной почты!")
    @NotBlank(message = "Электронная почта не может быть пустой!")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Необходимо указать имя пользователя!")
    @Column(nullable = false)
    private String name;
}
