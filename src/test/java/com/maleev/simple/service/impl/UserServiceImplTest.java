package com.maleev.simple.service.impl;

import com.maleev.simple.model.entity.User;
import com.maleev.simple.model.enums.Role;
import com.maleev.simple.repository.UserRepository;
import com.maleev.simple.service.MailSender;
import com.maleev.simple.service.UserService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Collections;
import java.util.Optional;

@ActiveProfiles("test")
@SpringJUnitConfig(UserServiceImplTest.TestConfig.class)
public class UserServiceImplTest {
    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private MailSender mailSender;
    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    public void whenAddUser() {
        User user = new User();
        user.setEmail("mail@mail.ru");
        boolean isUserCreated = userService.addUser(user);
        Assertions.assertTrue(isUserCreated);
        Assertions.assertNotNull(user.getActivationCode());
        Assertions.assertTrue(CoreMatchers.is(user.getRoles()).matches(Collections.singleton(Role.USER)));
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
        Mockito.verify(mailSender, Mockito.times(1))
                .send(
                        ArgumentMatchers.eq(user.getEmail()),
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString()
                );
    }

    @Test
    public void whenAddUserFailTest() {
        User user = new User();
        user.setUsername("Ivan");
        Mockito.when(userRepository
                .findByUsername("Ivan")).thenReturn(Optional.of(new User()));
        boolean isUserCreated = userService.addUser(user);
        Assertions.assertFalse(isUserCreated);
        Mockito.verify(userRepository, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
        Mockito.verify(mailSender, Mockito.times(0))
                .send(ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString());
    }

    @Test
    public void whenActivateUser() {
        User user = new User();
        user.setActivationCode("Accept!");
        Mockito.when(userRepository
                .findByActivationCode("activate")).thenReturn(Optional.of(user));
        boolean isUserActivated = userService.activateUser("activate");
        Assertions.assertTrue(isUserActivated);
        Assertions.assertNull(user.getActivationCode());
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    public void whrnActivateUserFailTest() {
        boolean isUserActivated = userService.activateUser("activate me");
        Assertions.assertFalse(isUserActivated);
        Mockito.verify(userRepository, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
    }

    @Profile("test")
    @TestConfiguration
    public static class TestConfig {
        @Bean
        public UserServiceImpl userService() {
            return new UserServiceImpl();
        }
    }
}