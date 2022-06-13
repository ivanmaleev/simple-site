package com.maleev.simple.service;

import com.maleev.simple.model.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Map;

/**
 * Сервис работы с пользователями
 */
public interface UserService extends UserDetailsService {

    /**
     * Добавляет нового пользователя
     *
     * @param user пользователь
     * @return true, если сохранено успешно, false если пользователь с таким username не найден
     */
    boolean addUser(User user);

    /**
     * Активирует пользователя
     *
     * @param code код активации
     * @return true в случае успеха
     */
    boolean activateUser(String code);

    /**
     * Сохраняет пользователя
     *
     * @param user     пользователь
     * @param username имя пользователя
     * @param form     форма
     */
    void save(User user, String username, Map<String, String> form);

    /**
     * Обновляет пользователя
     *
     * @param user     пользователь
     * @param password пароль пользователя
     * @param email    email пользователя
     */
    void updateProfile(User user, String password, String email);

    /**
     * Подписывает пользователя на другого
     *
     * @param currentUser текущий пользовтаель, который подписывается
     * @param user        пользователь, на которого подписываются
     */
    void subscribe(User currentUser, User user);

    /**
     * Отписывает пользователя на другого
     *
     * @param currentUser текущий пользовтаель, который отписывается
     * @param user        пользователь, на которого отписываются
     */
    void unsubscribe(User currentUser, User user);

    /**
     * Находит всех пользователей в БД
     *
     * @return список пользовталей
     */
    List<User> findAll();
}
