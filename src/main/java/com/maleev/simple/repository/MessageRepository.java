package com.maleev.simple.repository;

import com.maleev.simple.model.entity.Message;
import com.maleev.simple.model.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Long> {
    List<Message> findAll();
    List<Message> findAllByTag(String tag);
    List<Message> findAllByUser(User user);
}
