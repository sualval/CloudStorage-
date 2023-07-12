package ru.netology.cloudstorage.dto.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    @NotBlank(message = "username couldn't be empty")
    @Size(min = 2, max = 20, message = "username should be between 2-20 characters")
    String username;


}
