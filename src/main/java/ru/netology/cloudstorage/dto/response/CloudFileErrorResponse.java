package ru.netology.cloudstorage.dto.response;

import lombok.AccessLevel;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CloudFileErrorResponse {
    String message;
    long timestamp;

    public CloudFileErrorResponse(String message, long timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }
}
