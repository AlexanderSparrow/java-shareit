package ru.practicum.shareit.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Email(message = "Некорректный формат электронной почты!")
    @NotBlank(message = "Электронная почта не может быть пустой!")
    private String email;

    @NotBlank(message = "Необходимо указать имя пользователя!")
    private String name;

    @OneToMany(mappedBy = "booker")
    private Set<Booking> bookings = new LinkedHashSet<>();

    @OneToMany(mappedBy = "author")
    private Set<Comment> comments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "owner")
    private Set<Item> items = new LinkedHashSet<>();

    @OneToMany(mappedBy = "requestor")
    private Set<Request> requests = new LinkedHashSet<>();

}
