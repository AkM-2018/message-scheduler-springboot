package com.akm.messagescheduler.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.akm.messagescheduler.entity.Message;

@DataJpaTest
@ActiveProfiles("test")
public class MessageRepositoryTest {

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    TestEntityManager entityManager;

    private Message messagePast;

    private Message messageFuture;

    @BeforeEach
    void init() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSS");

        String timeStampPast = LocalDateTime.now().minusYears(1).format(formatter);
        String timeStampFuture = LocalDateTime.now().plusYears(1).format(formatter);

        messagePast = Message.builder().message("message text")
                .receiverNo("919876543210").scheduledAt(timeStampPast)
                .messageStatus("Pending").build();

        messageFuture = Message.builder().message("message text")
                .receiverNo("919876543210").scheduledAt(timeStampFuture)
                .messageStatus("Pending").build();

        messageRepository.save(messagePast);
        messageRepository.save(messageFuture);
    }

    @Test
    void testFindPendingMessages() {
        List<Message> expectedMessageList = new ArrayList<>();
        expectedMessageList.add(messagePast);

        List<Message> actualMessageList = messageRepository.findPendingMessages();

        assertEquals(expectedMessageList.size(), actualMessageList.size());
        for (int i = 0; i < expectedMessageList.size(); i++) {
            assertEquals(expectedMessageList.get(i).getMessage(), actualMessageList.get(i).getMessage());
            assertEquals(expectedMessageList.get(i).getScheduledAt(), actualMessageList.get(i).getScheduledAt());
            assertEquals(expectedMessageList.get(i).getReceiverNo(), actualMessageList.get(i).getReceiverNo());
            assertEquals(expectedMessageList.get(i).getMessageStatus(), actualMessageList.get(i).getMessageStatus());
        }
    }

    @Test
    void testFindScheduledMessages() {
        List<Message> expectedMessageList = new ArrayList<>();
        expectedMessageList.add(messageFuture);

        List<Message> actualMessageList = messageRepository.findScheduledMessages();

        assertEquals(expectedMessageList.size(), actualMessageList.size());
        for (int i = 0; i < expectedMessageList.size(); i++) {
            assertEquals(expectedMessageList.get(i).getMessage(), actualMessageList.get(i).getMessage());
            assertEquals(expectedMessageList.get(i).getScheduledAt(), actualMessageList.get(i).getScheduledAt());
            assertEquals(expectedMessageList.get(i).getReceiverNo(), actualMessageList.get(i).getReceiverNo());
            assertEquals(expectedMessageList.get(i).getMessageStatus(), actualMessageList.get(i).getMessageStatus());
        }
    }
}
