package ru.netology.cloudstorage.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Error Input Data")
public class CloudFileException extends RuntimeException{
    public CloudFileException(String message) {
        super(message);
    }
}
