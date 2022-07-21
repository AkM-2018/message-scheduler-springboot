package com.akm.messagescheduler.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.akm.messagescheduler.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("select m from Message m where m.messageStatus = 'Pending' and m.scheduledAt < now()")
    List<Message> findPendingMessages();

    @Query("select m from Message m where m.messageStatus = 'Pending' and m.scheduledAt >= now()")
    List<Message> findScheduledMessages();
}
