package ru.netology.cloudstorage.service;

import ru.netology.cloudstorage.entities.User;


public interface UserService {
    void registration(User user);

    User findByUsername(String username);

}
