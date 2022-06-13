package com.maleev.simple.service;

import com.maleev.simple.model.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Map;

public interface UserService extends UserDetailsService {
    boolean addUser(User user);

    boolean activateUser(String code);

    void save(User user, String username, Map<String, String> form);

    void updateProfile(User user, String password, String email);

    void subscribe(User currentUser, User user);

    void unsubscribe(User currentUser, User user);

    List<User> findAll();
}
