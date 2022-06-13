package com.maleev.simple.service;

import org.springframework.stereotype.Service;

/**
 * Сервис работы с почтой
 */
@Service
public interface MailSender {

    /**
     * Посылает сообщение по почте
     *
     * @param emailTo почта получается
     * @param subject тема письма
     * @param message сообщение
     */
    void send(String emailTo, String subject, String message);
}
