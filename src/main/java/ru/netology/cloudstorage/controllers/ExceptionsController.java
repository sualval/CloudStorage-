package ru.netology.cloudstorage.controllers;

import ru.netology.cloudstorage.util.exceptions.CloudFileException;
import ru.netology.cloudstorage.util.exceptions.UsernameNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class ExceptionsController extends ResponseEntityExceptionHandler {


    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<Object> UserBadCredentialsExceptions() {
        return ResponseEntity.badRequest().body("Bad credentials");
    }


    @ExceptionHandler({CloudFileException.class})
    public ResponseEntity<Object> CloudFileException(CloudFileException e) {
        return ResponseEntity.badRequest().body("Error input data");

    }
}
