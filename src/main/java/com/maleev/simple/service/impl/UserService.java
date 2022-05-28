package com.maleev.simple.service.impl;

import com.maleev.simple.model.entity.User;
import com.maleev.simple.model.enums.Role;
import com.maleev.simple.repository.UserRepository;
import com.maleev.simple.service.MailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final MailSender mailSender;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).get();
    }

    public boolean addUser(User user) {
        Optional<User> userFounded = userRepository.findByUsername(user.getUsername());
        if (userFounded.isPresent()) {
            return false;
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to Simple-site. Please, visit next link: http://localhost:8099/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );
            try {
                mailSender.send(user.getEmail(), "Activation code", message);
            } catch (MailException e) {
                e.printStackTrace();
                return false;
            }
        }
        userRepository.save(user);
        return true;
    }

    public boolean activateUser(String code) {
        Optional<User> userFounded = userRepository.findByActivationCode(code);
        if (!userFounded.isPresent()) {
            return false;
        }
        userFounded.get().setActivationCode(null);
        userRepository.save(userFounded.get());
        return true;
    }
}
