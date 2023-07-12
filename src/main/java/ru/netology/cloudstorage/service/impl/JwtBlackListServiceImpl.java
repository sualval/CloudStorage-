package ru.netology.cloudstorage.service.impl;


import ru.netology.cloudstorage.service.JwtBlackListService;
import ru.netology.cloudstorage.entities.JwtBlackListEntity;
import ru.netology.cloudstorage.repositories.JwtBlackListRepository;
import ru.netology.cloudstorage.security.jwt.JwtUtils;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class JwtBlackListServiceImpl implements JwtBlackListService {
    JwtBlackListRepository jwtBlackListRepository;
    JwtUtils jwtUtils;

    @Autowired
    public JwtBlackListServiceImpl(JwtBlackListRepository jwtBlackListRepository, JwtUtils jwtUtils) {
        this.jwtBlackListRepository = jwtBlackListRepository;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public JwtBlackListEntity saveInBlackList(String jwt) {
        Long exp = jwtUtils.extractExpiration(jwt).getTime();
        return jwtBlackListRepository.saveAndFlush(new JwtBlackListEntity(jwt, exp));
    }

    @Override
    public boolean isBlacklisted(String jwt) {
        return jwtBlackListRepository.findByJwtEquals(jwt).isPresent();
    }
}

