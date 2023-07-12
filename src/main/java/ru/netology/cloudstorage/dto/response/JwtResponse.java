package ru.netology.cloudstorage.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class JwtResponse {
    boolean success;
    @JsonProperty("auth-token")
    String authToken;

}
