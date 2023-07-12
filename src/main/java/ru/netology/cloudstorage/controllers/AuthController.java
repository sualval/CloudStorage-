package ru.netology.cloudstorage.controllers;

import ru.netology.cloudstorage.entities.JwtBlackListEntity;
import ru.netology.cloudstorage.entities.User;
import ru.netology.cloudstorage.service.JwtBlackListService;
import ru.netology.cloudstorage.service.UserService;
import ru.netology.cloudstorage.dto.request.LoginRequest;
import ru.netology.cloudstorage.dto.request.SignupRequest;
import ru.netology.cloudstorage.dto.response.JwtResponse;
import ru.netology.cloudstorage.security.jwt.JwtUtils;

import ru.netology.cloudstorage.util.mapper.UserMapper;
import ru.netology.cloudstorage.util.validator.ResponseErrorValidator;
import ru.netology.cloudstorage.util.validator.UserValidator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("permitAll()")
@CrossOrigin
public class AuthController {

    AuthenticationManager authenticationManager;
    JwtUtils jwtUtils;
    UserService userService;
    ResponseErrorValidator responseErrorValidator;
    UserMapper userMapper;
    UserValidator userValidator;
    JwtBlackListService blackListService;


    @PostMapping("/login")
    ResponseEntity<?> login(@Valid @RequestBody LoginRequest authenticationDto) {

        var authenticationToken = new UsernamePasswordAuthenticationToken(authenticationDto.getLogin(),
                authenticationDto.getPassword());
        authenticationManager.authenticate(authenticationToken);
        String token = jwtUtils.generateToken(authenticationDto.getLogin());
        return ResponseEntity.ok(new JwtResponse(true, token));
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response,
                                         @RequestHeader("auth-token") String authToken) {
        String jwt = authToken.substring(7);
        JwtBlackListEntity blackListToken = blackListService.saveInBlackList(jwt);

        if (blackListToken == null) {
            log.error("Something went wrong. Logout failed");
            return new ResponseEntity<>("Something went wrong",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        return ResponseEntity.ok("Success logout");

    }


    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody SignupRequest signupRequest,
                                               BindingResult bindingResult) {
        User user = userMapper.convertToSignupUser(signupRequest);
        userValidator.validate(user, bindingResult);
        ResponseEntity<Object> errors = responseErrorValidator.mapValidationService(bindingResult);
        if (errors != null) {
            return errors;
        }
        userService.registration(user);

        return ResponseEntity.ok("User registered successfully");


    }
}
