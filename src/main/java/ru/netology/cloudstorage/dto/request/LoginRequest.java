package ru.netology.cloudstorage.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest implements Serializable {

    @NotEmpty(message = "username couldn't be empty")
    @Size(min = 2, max = 20, message = "username should be between 2-20 characters")
    String login;

    @NotEmpty(message = "password couldn't be empty")
    @Size(min = 3, max = 20, message = "a password should be between 3-20 characters")
    String password;
}
