package com.maleev.simple.repository;

import com.maleev.simple.model.entity.Message;
import com.maleev.simple.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Long> {

    Page<Message> findAll(Pageable pageable);

    Page<Message> findAllByTag(String tag, Pageable pageable);

    List<Message> findAllByUser(User user);
}
