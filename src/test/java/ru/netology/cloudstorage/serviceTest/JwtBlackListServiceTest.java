package ru.netology.cloudstorage.serviceTest;


import ru.netology.cloudstorage.entities.JwtBlackListEntity;
import ru.netology.cloudstorage.repositories.JwtBlackListRepository;
import ru.netology.cloudstorage.service.impl.JwtBlackListServiceImpl;
import ru.netology.cloudstorage.security.jwt.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;


import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class JwtBlackListServiceTest {
    @InjectMocks
    private JwtBlackListServiceImpl jwtBlackListService;
    @Mock
    private JwtBlackListEntity jwtBlackListEntity;
    @Mock
    private JwtBlackListRepository jwtBlackListRepository;
    @Mock
    private JwtUtils utils;

    @BeforeEach
    void setUp() {
        openMocks(this);
        jwtBlackListEntity = new JwtBlackListEntity("a", 1L);
    }

    @Test
    void saveInBlackList() {
        when(utils.extractExpiration(anyString())).thenReturn(new Date());
        when(jwtBlackListRepository.saveAndFlush(any(JwtBlackListEntity.class)))
                .thenReturn(jwtBlackListEntity);
        jwtBlackListService.saveInBlackList(anyString());

        verify(utils, times(1)).extractExpiration(anyString());
        verify(jwtBlackListRepository, times(1)).saveAndFlush(any(JwtBlackListEntity.class));
    }

    @Test
    void isBlacklisted() {
        when(jwtBlackListRepository.findByJwtEquals(anyString())).thenReturn(Optional.of(jwtBlackListEntity));

        jwtBlackListService.isBlacklisted(anyString());

        verify(jwtBlackListRepository, times(1)).findByJwtEquals(anyString());
    }

}
