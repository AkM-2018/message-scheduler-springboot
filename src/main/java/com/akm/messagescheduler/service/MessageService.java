package com.akm.messagescheduler.service;

import java.util.List;

import com.akm.messagescheduler.dto.MessageRequest;
import com.akm.messagescheduler.entity.Message;
import com.akm.messagescheduler.error.EmptyFieldException;
import com.akm.messagescheduler.error.InvalidDateException;
import com.akm.messagescheduler.error.InvalidReceiverNumberException;

public interface MessageService {

    Message saveMessage(MessageRequest messageRequest)
            throws EmptyFieldException, InvalidDateException, InvalidReceiverNumberException;

    Message updateMessageById(Long messageId, Message message);

    List<Message> findScheduledMessages();

}
