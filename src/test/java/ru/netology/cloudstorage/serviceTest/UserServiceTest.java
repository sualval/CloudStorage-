package ru.netology.cloudstorage.serviceTest;

import ru.netology.cloudstorage.repositories.UserRepository;
import ru.netology.cloudstorage.service.impl.UserServiceImpl;
import ru.netology.cloudstorage.entities.Role;
import ru.netology.cloudstorage.entities.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class UserServiceTest {
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;


    @Mock
    private User userStub;

    private String defaultUsername = "user";
    private String defaultPassword = "pass";
    private Role defaultRole = Role.valueOf("ROLE_USER");

    @BeforeEach
    void setUp() {
        openMocks(this);
    }


    @Test
    void willShowTrueIfSaveUser() {
        when(userRepository.save(any(User.class))).thenReturn(
                new User(defaultUsername, defaultPassword, defaultRole)
        );

        User savedUser = userRepository.save(userStub);

        Assertions.assertNotNull(savedUser);
        verify(userRepository, times(1)).save(any(User.class));

    }

    @Test
    void testFindByUsernameShouldBeNotNull() {

        when(userRepository.findByLogin(defaultUsername))
                .thenReturn(Optional.of(userStub));

        User userExpected = userService.findByUsername(defaultUsername);
        Assertions.assertNotNull(userExpected);

        verify(userRepository, times(1)).findByLogin(defaultUsername);
    }





}
