package ru.netology.cloudstorage.util.validator;


import ru.netology.cloudstorage.entities.User;
import ru.netology.cloudstorage.service.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserValidator implements Validator {

    UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        User userExist = userService.findByUsername(user.getLogin());
        if (userExist != null) {
            errors.rejectValue("login", "400", "user with a such username already exists");
        }

    }
}