package com.maleev.simple.service.impl;

import com.maleev.simple.model.entity.User;
import com.maleev.simple.model.enums.Role;
import com.maleev.simple.repository.UserRepository;
import com.maleev.simple.service.MailSender;
import com.maleev.simple.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Имплементация сервиса работы с пользователями
 */
@Service
@NoArgsConstructor
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return user.get();
    }

    @Override
    public boolean addUser(User user) {
        Optional<User> userFounded = userRepository.findByUsername(user.getUsername());
        if (userFounded.isPresent()) {
            return false;
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (sendMessage(user)) {
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean activateUser(String code) {
        Optional<User> userFounded = userRepository.findByActivationCode(code);
        if (!userFounded.isPresent()) {
            return false;
        }
        userFounded.get().setActivationCode(null);
        userRepository.save(userFounded.get());
        return true;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void save(User user, String username, Map<String, String> form) {
        user.setUsername(username);
        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());
        user.getRoles().clear();
        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }
        userRepository.save(user);
    }

    @Override
    public void updateProfile(User user, String password, String email) {
        String userEmail = user.getEmail();
        boolean isEmailChanged = (email != null && !email.equals(userEmail)) ||
                (userEmail != null && !userEmail.equals(email));
        if (isEmailChanged) {
            user.setEmail(email);
            if (!StringUtils.isEmpty(email)) {
                user.setActivationCode(UUID.randomUUID().toString());
            }
        }
        if (!StringUtils.isEmpty(password)) {
            user.setPassword(password);
        }
        userRepository.save(user);
        if (isEmailChanged) {
            sendMessage(user);
        }
    }

    private boolean sendMessage(User user) {
        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to Simple-site. Please, visit next link: http://localhost:8099/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );
            try {
                mailSender.send(user.getEmail(), "Activation code", message);
                return true;
            } catch (MailException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public void subscribe(User currentUser, User user) {
        user.getSubscribers().add(currentUser);
        userRepository.save(user);
    }

    @Override
    public void unsubscribe(User currentUser, User user) {
        user.getSubscribers().remove(currentUser);
        userRepository.save(user);
    }
}
