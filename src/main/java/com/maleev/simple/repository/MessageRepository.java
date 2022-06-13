package com.maleev.simple.repository;

import com.maleev.simple.model.dto.MessageDto;
import com.maleev.simple.model.entity.Message;
import com.maleev.simple.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * репозиторий по работе с сущностью {@link Message}
 */
public interface MessageRepository extends CrudRepository<Message, Long> {

    @Query("SELECT new com.maleev.simple.model.dto.MessageDto(m, count(ml), sum(case when ml = :user then 1 else 0 end) > 0) \n" +
            "FROM Message AS m \n" +
            "LEFT JOIN m.likes ml \n" +
            "GROUP BY m")
    Page<MessageDto> findAll(Pageable pageable, @Param("user") User user);

    @Query("SELECT new com.maleev.simple.model.dto.MessageDto(m, count(ml), sum(case when ml = :user then 1 else 0 end) > 0) \n" +
            "FROM Message AS m \n" +
            "LEFT JOIN m.likes ml \n" +
            "WHERE m.tag = :tag \n" +
            "GROUP BY m")
    Page<MessageDto> findByTag(@Param("tag") String tag, Pageable pageable, @Param("user") User user);

    @Query("SELECT new com.maleev.simple.model.dto.MessageDto(m, count(ml), sum(case when ml = :user then 1 else 0 end) > 0) \n" +
            "FROM Message AS m \n" +
            "LEFT JOIN m.likes ml \n" +
            "WHERE m.author = :author \n" +
            "GROUP BY m")
    Page<MessageDto> findByUser(Pageable pageable, @Param("author") User author, @Param("user") User user);
}
