package com.maleev.simple.repository;

import com.maleev.simple.model.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * репозиторий по работе с сущностью {@link User}
 */
public interface UserRepository extends CrudRepository<User, Long> {

    @Query(value = "SELECT user \n" +
            "FROM User user \n" +
            "LEFT JOIN FETCH user.messages \n" +
            "WHERE user.username = ?1")
    Optional<User> findByUsername(String username);

    Optional<User> findByActivationCode(String activationCode);
    List<User> findAll();
}
