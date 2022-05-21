package com.maleev.simple.repository;

import com.maleev.simple.entity.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Long> {
    List<Message> findAll();
    List<Message> findAllByTag(String tag);
}
