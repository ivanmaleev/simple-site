package com.maleev.simple.service;

import com.maleev.simple.model.dto.MessageDto;
import com.maleev.simple.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Сервис работы с сообщениями
 */
@Service
public interface MessageService {

    /**
     * Возвращает список сообщений пользователя по тэгу
     *
     * @param pageable интерфейс для постраничного вывода
     * @param filter   фильтр тэга
     * @param user     пользователь
     * @return список сообщений
     */
    Page<MessageDto> messageList(Pageable pageable, String filter, User user);

    /**
     * Возвращает список сообщений пользователя
     *
     * @param pageable    интерфейс для постраничного вывода
     * @param currentUser текущий пользователя
     * @param author      автор сообщений
     * @return список сообщений
     */
    Page<MessageDto> messageListForUser(Pageable pageable, User currentUser, User author);
}
