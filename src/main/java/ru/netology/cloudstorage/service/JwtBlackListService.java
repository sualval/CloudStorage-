package ru.netology.cloudstorage.service;

import ru.netology.cloudstorage.entities.JwtBlackListEntity;

public interface JwtBlackListService {

    boolean isBlacklisted(String jwt);

    JwtBlackListEntity saveInBlackList(String jwt);
}
