package com.maleev.simple.service.impl;

import com.maleev.simple.model.dto.MessageDto;
import com.maleev.simple.model.entity.User;
import com.maleev.simple.repository.MessageRepository;
import com.maleev.simple.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Имплементация сервиса работы с сообщениями
 */
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public Page<MessageDto> messageList(Pageable pageable, String filter, User user) {
        if (filter != null && !filter.isEmpty()) {
            return messageRepository.findByTag(filter, pageable, user);
        } else {
            return messageRepository.findAll(pageable, user);
        }
    }

    @Override
    public Page<MessageDto> messageListForUser(Pageable pageable, User currentUser, User author) {
        return messageRepository.findByUser(pageable, author, currentUser);
    }
}